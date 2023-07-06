package com.jnetdata.msp.manage.plantask.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.plantask.CommonConstant;
import com.jnetdata.msp.manage.plantask.job.*;
import com.jnetdata.msp.manage.plantask.mapper.QuartzJobMapper;
import com.jnetdata.msp.manage.plantask.model.QuartzJob;
import com.jnetdata.msp.manage.plantask.service.IQuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJobMapper, QuartzJob> implements IQuartzJobService {

	@Autowired
	@Lazy
	private TaskModel taskBean;

	@Override
	protected PropertyWrapper<QuartzJob> makeSearchWrapper(Map<String, Object> condition) {
		return createWrapperUtil(condition).eq("id").eq("jobClassName").eq("status").getWrapper();
	}

	@Override
	public List<QuartzJob> findByJobClassName(String jobClassName) {
		List<QuartzJob> list = super.list(new PropertyWrapper<>(QuartzJob.class).eq("jobClassName", jobClassName));
		return list;
	}

	/**
	 * 保存&启动定时任务
	 */
	@Override
	public boolean saveAndScheduleJob(QuartzJob quartzJob) {
		if (CommonConstant.STATUS_NORMAL.equals(quartzJob.getStatus())) {
			taskBean.startCron();
			return true;
		}
		return false;
	}

	/**
	 * 编辑&启停定时任务
	 */
	@Override
	public boolean editAndScheduleJob(QuartzJob quartzJob) {
		boolean b = this.updateById(quartzJob);
		if (b) {
			taskBean.startCron();
		}
		quartzJob.setOldjobClassName(quartzJob.getJobClassName());
		b = this.updateById(quartzJob);
		return b;
	}

	/**
	 * 删除&停止删除定时任务
	 */
	@Override
	public boolean deleteAndStopJob(QuartzJob job) {
		boolean ok = this.deleteById(job.getId());
		return ok;
	}

}
