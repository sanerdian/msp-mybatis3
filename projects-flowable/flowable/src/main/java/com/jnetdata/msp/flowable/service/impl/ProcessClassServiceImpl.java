package com.jnetdata.msp.flowable.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.flowable.enums.Logic;
import com.jnetdata.msp.flowable.mapper.ProcessClassMapper;
import com.jnetdata.msp.flowable.mapper.ProcessDefiMapper;
import com.jnetdata.msp.flowable.model.ProcessClass;
import com.jnetdata.msp.flowable.model.ProcessDefi;
import com.jnetdata.msp.flowable.service.ProcessClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class ProcessClassServiceImpl extends BaseServiceImpl<ProcessClassMapper, ProcessClass> implements ProcessClassService {

    @Resource
    private ProcessClassMapper processClassMapper;

    @Resource
    private ProcessDefiMapper processDefiMapper;

    @Override
    protected PropertyWrapper<ProcessClass> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("className")
                .eq("deleteFlag")
                .getWrapper();
    }

    /**
     * 获取新对象序号
     */
    @Override
    public int getOrderNumber(){
        try {
            int max = processClassMapper.getOrderNumber();
            return max+1;
        }catch (Exception e){
            log.error("获取新对象序号异常：{}", e.getMessage());
        }
        return 1;
    }

    /**
     * 上移
     */
    @Override
    public JsonResult<Void> moveUp(Integer id){
        try {
            this.moveCommon(id, false);
        }catch (Exception e){
            log.error("上移异常：{}", e.getMessage());
            return JsonResult.fail();
        }
        return JsonResult.success();
    }

    /**
     * 下移
     */
    @Override
    public JsonResult<Void> moveDown(Integer id){
        try {
            this.moveCommon(id, true);
        }catch (Exception e){
            log.error("下移异常：{}", e.getMessage());
            return JsonResult.fail();
        }
        return JsonResult.success();
    }

    /**
     * 上移和下移公用方法
     * @param id 分类id
     * @param isAsc 排序规则，上移时为false， 下移时为true
     */
    private void moveCommon(Integer id, boolean isAsc){
        //获取分类信息
        ProcessClass po = this.getById(id);

        //获取大于（或小于）当前序号的分类信息列表
        List<String> orderList = new ArrayList<>();
        orderList.add("orderNumber");
        PropertyWrapper wrapper = new PropertyWrapper<>(ProcessClass.class);
        if(isAsc){
            //下移时，查询大于当前序号的数据
            wrapper.gt("orderNumber", po.getOrderNumber());
        }else{
            //上移时，查询小于当前序号的数据
            wrapper.lt("orderNumber", po.getOrderNumber());
        }
        wrapper.eq("deleteFlag", Logic.NO.getCode()).orderBy(orderList, isAsc);
        List<ProcessClass> list = this.list(wrapper);

        //如果列表不为空，列表第一个分类与当前分类互换顺序
        if(!CollectionUtils.isEmpty(list)){
            ProcessClass entity1 = new ProcessClass();
            ProcessClass entity2 = new ProcessClass();
            Date now = new Date();

            entity1.setId(id);
            entity1.setUpdateTime(now);
            entity1.setOrderNumber(list.get(0).getOrderNumber());
            this.updateById(entity1);

            entity2.setId(list.get(0).getId());
            entity2.setUpdateTime(now);
            entity2.setOrderNumber(po.getOrderNumber());
            this.updateById(entity2);
        }
    }

    /**
     * 设置分类下的流程信息
     */
    @Override
    public void setProcessList(List<ProcessClass> classList){
        try {
            //排序字段
            List<String> orderList = new ArrayList<>();
            orderList.add("orderNumber");

            //查询分类下的流程列表
            for(ProcessClass processClass: classList){
                List<ProcessDefi> procList = processDefiMapper.selectList(new PropertyWrapper<>(ProcessDefi.class)
                        .eq("processClassId", processClass.getId())
                        .orderBy(orderList, true).getWrapper());
                processClass.setProcList(procList);
            }
        }catch (Exception e){
            log.error("设置分类下的流程信息异常：{}", e.getMessage());
        }
    }
}
