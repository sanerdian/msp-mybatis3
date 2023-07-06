package com.jnetdata.msp.manage.plantask.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.plantask.model.PlanTask;

import java.util.List;

public interface PlanTaskService extends BaseService<PlanTask> {

    List<PlanTask> getPlanTask(PlanTask planTask);
}
