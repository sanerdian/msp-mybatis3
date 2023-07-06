package com.jnetdata.msp.metadata.pushg.service.impl;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.metadata.pushg.mapper.PushgMapper;
import com.jnetdata.msp.metadata.pushg.model.PushgModel;
import com.jnetdata.msp.metadata.pushg.service.PushgService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

@Service
public class PushgServiceImpl extends BaseServiceImpl<PushgMapper, PushgModel> implements PushgService {

    private String andOr = "and";
    //服务实现类
    @Override
    public PropertyWrapper<PushgModel> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) {
            this.andOr = condition.get("andOr").toString();
        }
        PropertyWrapper<PushgModel> pw = createWrapperUtil(condition)
                .eq("id")
                .eq("groupname")
                .eq("pushtogroups")
                .eq("pushtousers")
                .eq("pushtoconditions")
                .eq("dwbsm")
                .eq("createBy")
                .eq("modifyTime")
                .eq("flowId")
                .getWrapper();
        andOr(condition,pw,"modifyUser");
        andOr(condition,pw,"flowUser");
        return pw;
    }
    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&& StringUtils.isNotEmpty(condition.get(proName).toString())){
            pw.andNew().where(andOr.equals("and")?"1=1":"1=2",null);
            for(String str:condition.get(proName).toString().split(" " )){
                if(StringUtils.isNotEmpty(str)){
                    if(andOr.equals("or" )){
                        pw.or().like(proName,str);
                    }else{
                        pw.and().like(proName,str);
                    }
                }
            }
        }
    }

}
