package com.jnetdata.msp.member.limit.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.limit.mapper.LimitMapper;
import com.jnetdata.msp.member.limit.model.Limit;
import com.jnetdata.msp.member.limit.service.LimitService;
import com.jnetdata.msp.member.limit.vo.LimitRerlationTypeVo;
import com.jnetdata.msp.member.role.service.RoleUserService;
import com.jnetdata.msp.member.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.*;

/**
 * Created by WFJ on 2019/4/2.
 */
@Service
public class LimitServiceImpl extends BaseServiceImpl<LimitMapper,Limit> implements LimitService {

    @Autowired
    UserService userService;
    @Autowired
    RoleUserService roleUserService;

    /**
     * 模糊查询
     * @param condition
     * @return
     */
    @Override
    protected PropertyWrapper<Limit> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("name")
                .eq("type")
                .getWrapper();
    }

    /**
     * 根据vo查询
     * @param vo
     * @return
     */
    @Override
    public List<Limit> list(Limit vo) {
        return list(createWrapper().eq("system",vo.getSystem()));
    }

    /**
     * 获取权限map集合（进行分类）
     * @return
     */
    private  Map<String,List<Limit>> getLimitMap(List<Limit> list){
        Map<String,List<Limit>> map = new LinkedHashMap<>();
        list.forEach(res->{
            if(!map.containsKey(String.valueOf(res.getType()))){
                List<Limit> tempList = new ArrayList<>();
                tempList.add(res);
                map.put(String.valueOf(res.getType()),tempList);
            }else{
                map.get(String.valueOf(res.getType())).add(res);
            }
        });

        return map;
    }

    /**
     * 把权限处理为需要的数据结构
     * @param map
     * @return
     */
    private List<LimitRerlationTypeVo> getLimitData(Map<String,List<Limit>> map){
        List<LimitRerlationTypeVo> list = new ArrayList<>();
        map.forEach((key,value)->{
            LimitRerlationTypeVo vo = new LimitRerlationTypeVo();
            if("1".equals(key)){
                vo.setName("站点权限");
                vo.setList(value);
            }else if("2".equals(key)){
                vo.setName("栏目权限");
                vo.setList(value);
            }else if("3".equals(key)){
                vo.setName("栏目的文档权限");
                vo.setList(value);
            }else if("4".equals(key)){
                vo.setName("公司权限");
                vo.setList(value);
            }else if("5".equals(key)){
                vo.setName("文档的文档权限");
                vo.setList(value);
            }else if("6".equals(key)){
                vo.setName("菜单权限");
                vo.setList(value);
            }else if("7".equals(key)){
                vo.setName("模板权限");
                vo.setList(value);
            }
            list.add(vo);
        });
        return list;
    }

}
