package com.jnetdata.msp.media.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.media.model.JobApi;
import com.jnetdata.msp.media.vo.push.SimpleWorkerVo;
import com.jnetdata.msp.media.vo.push.TreeNodeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PushMapper extends BaseMapper<JobApi> {
    List<TreeNodeVo> loadDwRoot(@Param("dwbsm")String dwbsm);
    /**查询单位：指定父单位标识码*/
    List<TreeNodeVo> listDwByFdwbsm(@Param("fdwbsm")String fdwbsm);
    /**查询单位：指定单位名称(是从树里面查询)*/
    List<TreeNodeVo> listDwByDwmc(@Param("fdwbsm")String fdwbsm,@Param("dwmc")String dwmc);

    /**查询机构：指定单位标识码*/
    List<TreeNodeVo> listJgByDwbsm(@Param("dwbsm")String dwbsm);
    /**查询机构：指定部门名称*/
    List<TreeNodeVo> listJgByBmmc(@Param("dwbsm")String dwbsm,@Param("bmmc")String bmmc);
    /**统计worker总数，使用对象中的条件*/
    int countWorker(@Param("entity") SimpleWorkerVo entity);
    /**分页查询worker，使用对象中的条件，start为起始行号-包含，end为结束行号-包含*/
    List<SimpleWorkerVo> listWorker(@Param("entity")SimpleWorkerVo entity, @Param("start") int start, @Param("end") int end);
    /**统计worker表中指定字段的取值*/
    List<String> distinctWorkerField(@Param("fieldName")String fieldName, @Param("bmbms")String bmbms, @Param("dwbsms")String dwbsms);

    List<SimpleWorkerVo> queryWorker(@Param("conditionStr")String conditionStr);
}
