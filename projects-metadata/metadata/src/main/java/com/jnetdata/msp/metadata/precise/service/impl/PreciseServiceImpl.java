package com.jnetdata.msp.metadata.precise.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.user.controller.UserController;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.metadata.precise.mapper.PreciseMapper;
import com.jnetdata.msp.metadata.precise.model.PreciseModel;
import com.jnetdata.msp.metadata.precise.service.PreciseService;
import com.jnetdata.msp.metadata.precise.vo.PreciseVo;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PreciseServiceImpl extends BaseServiceImpl<PreciseMapper, PreciseModel> implements PreciseService {

    @Resource
    private UserController userController;

    @Override
    public Boolean addlist(PreciseVo entity){

        int status = entity.getPicname();
        if(status==1){
            PreciseModel preciseModel = new PreciseModel();
            preciseModel.setClassid(entity.getId());
            preciseModel.setUserid(entity.getUserid());
            preciseModel.setUsername(entity.getName());
            preciseModel.setPicname(1);//单位推送 1为单位，0为用户
            preciseModel.setIsdisplay(entity.getIsdisplay());//是否包含子部门
            int insert = baseMapper.insert(preciseModel);
            return true;

        }else{
            PreciseModel preciseModel = new PreciseModel();
            Long id = entity.getUserid();
            String sex = entity.getSex();
            String office = entity.getOffice();
            int age = entity.getAge();
            String number = entity.getNumber();
            List<User> users= userController.selectget(id,sex,office,age,number);
            int a=0;
            ArrayList<PreciseModel> preciseModels = new ArrayList<>();
            users.forEach(s->{
                preciseModel.setUserid(s.getId());
                preciseModel.setUsername(s.getName());
                preciseModel.setClassid(entity.getId());
                preciseModel.setPicname(0);
                preciseModels.add(preciseModel);
                baseMapper.insert(preciseModel);
            });
            if (preciseModels!=null){
                return true;
            }
            return false;

        }
    }
}
