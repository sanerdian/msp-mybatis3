package com.jnetdata.msp.manage.plantask.service;


import com.baomidou.mybatisplus.service.IService;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.plantask.model.QuartzJob;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IQuartzJobService extends BaseService<QuartzJob> {

	List<QuartzJob> findByJobClassName(String jobClassName);

	boolean saveAndScheduleJob(QuartzJob quartzJob);

	boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

	boolean deleteAndStopJob(QuartzJob quartzJob);
}
