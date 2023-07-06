package com.jnetdata.msp.log.metadata.service.impl;

import com.google.common.base.Joiner;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.metadata.mapper.MetadataLogMapper;
import com.jnetdata.msp.log.metadata.model.MetadataLog;
import com.jnetdata.msp.log.metadata.service.MetadataLogService;
import com.jnetdata.msp.util.mapper.FieldinfoUtilMapper;
import com.jnetdata.msp.util.model.Fieldinfo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by TF on 2019/3/13.
 */
@Service
public class MetadataLogServiceImpl extends BaseServiceImpl<MetadataLogMapper,MetadataLog> implements MetadataLogService {

    @Override
    protected PropertyWrapper<MetadataLog> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .like("title")
                .like("handleType")
                .like("name")
                .like("detail")
                .like("result")
                .like("crUser")
                .between("createTime","endTime")
                .like("address")
                .getWrapper();
    }
    @Resource
    private FieldinfoUtilMapper fieldinfoUtilMapper;
    @Override
    public void addFieldLog(Map<String, Object> old, Map<String, Object> newObj,String ip,Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MetadataLog log = new MetadataLog();
        log.setHandleType("修改字段");
        log.setName(old.get("tablename")+"");
        log.setTitle(old.get("anothername")+"");
        log.setAddress(ip);
        Object fieldtype = newObj.get("fieldtype");
        List<String> detail = new ArrayList<>();
        if(!newObj.get("tablename").equals(old.get("tablename"))){
            detail.add("元数据表名由"+old.get("tablename")+"修改为"+newObj.get("tablename"));
        } if(!newObj.get("fieldname").equals(old.get("fieldname"))){
            detail.add("英文名由"+old.get("fieldname")+"修改为"+newObj.get("fieldname"));
        } if(!newObj.get("anothername").equals(old.get("anothername"))){
            detail.add("中文文名由"+old.get("anothername")+"修改为"+newObj.get("anothername"));
        } if(!newObj.get("dblength").equals(old.get("dblength"))&&!newObj.get("dblength").equals("")){
            detail.add("库中长度由"+old.get("dblength")+"修改为"+newObj.get("dblength"));
        } if(!newObj.get("fieldtype").equals(old.get("fieldtype"))){
            List<Map> maps = baseMapper.selectType();
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            maps.forEach(s->{
                if(s.get("no").equals(old.get("fieldtype"))){
                    String s1 = s.get("name").toString();
                    stringStringHashMap.put("s1",s1);
                }
                if(s.get("no").equals(newObj.get("fieldtype"))){
                    String s2= s.get("name").toString();
                    stringStringHashMap.put("s2",s2);
                }
            });
            detail.add("字段类型"+stringStringHashMap.get("s1")+"修改为"+stringStringHashMap.get("s1"));
        }if(!newObj.get("dbtype").equals(old.get("dbtype"))){
            List<Map> maps = baseMapper.selectDbtype();
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            maps.forEach(s->{
                if(s.get("no").equals(old.get("dbtype"))){
                    String s1 = s.get("name").toString();
                    stringStringHashMap.put("s1",s1);
                }
                if(s.get("no").equals(newObj.get("dbtype"))){
                    String s2= s.get("name").toString();
                    stringStringHashMap.put("s2",s2);
                }
            });
            detail.add("库中类型"+stringStringHashMap.get("s1")+"修改为"+stringStringHashMap.get("s1"));
        }if(!newObj.get("matchType").equals(old.get("matchType"))){
            Object matchType = (Integer)newObj.get("matchType");
            String newMatchType = selectMatchType((Integer) newObj.get("matchType"));
            String oldMatchType = selectMatchType((Integer) old.get("matchType"));
            detail.add("匹配方式"+oldMatchType+"修改为"+newMatchType);

        }if(!newObj.get("istitle").equals(old.get("istitle"))){
            String newIs= (Integer)newObj.get("istitle")==0?"否":"是";
            String oldIs= (Integer)old.get("istitle")==0?"否":"是";
            detail.add("是否为标题"+oldIs+"修改为"+newIs);
        }if(!newObj.get("groupid").equals(old.get("groupid"))){
            String newGroup = baseMapper.selectGroup( Long.valueOf(newObj.get("groupid").toString()));
            String oldGroup = baseMapper.selectGroup(Long.valueOf(old.get("groupid").toString()));
            detail.add("是否为标题"+oldGroup+"修改为"+newGroup);
        }/*if(!newObj.get("notnull").equals(old.get("notnull"))){
            String newIs= (Integer)newObj.get("notnull")==0?"否":"是";
            String oldIs= (Integer)old.get("notnull")==0?"否":"是";
            detail.add("是否为空"+oldIs+"修改为"+newIs);
        }*/
       /* for (Object key : newObj.keySet()) {
            if(newObj.get(key).equals(old.get(key))) continue;
            if(key.equals("modifyTime")){
                detail.add("修改时间为"+":"+sdf.format(newObj.get(key)));
                continue;
            };
            if(!key.equals("proName"))
            detail.add(key +":"+old.get(key)+"修改为"+newObj.get(key));
        }*/
        log.setModifyTime(new Date());
        log.setModifyUser(log.getCrUser());
        Fieldinfo fieldinfo = fieldinfoUtilMapper.selectById(id);
        log.setCreateTime(fieldinfo.getCrtime());
        log.setCrUser(fieldinfo.getCruser());
        log.setModifyUser(fieldinfo.getCruser());

        if(detail.size() == 0) return;
        log.setDetail(Joiner.on("；").join(detail));
        super.insert(log);
    }

    public String selectMatchType(Integer matchType){
        switch (matchType){
            case 1: return ">=";
            case 2: return ">!=";
            case 3: return ">";
            case 4: return ">=";
            case 5: return "<";
            case 6: return "<=";
            case 7: return "between";
            case 8: return "like";
            case 9: return "全文检索";
        }
        return null;
    }

    @Override
    public void addlogd( Map<String, Object> newObj,String ip) {
        val user = SessionUser.getCurrentUser();
        String name = user.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MetadataLog log = new MetadataLog();
        log.setHandleType("删除字段");
        log.setName(newObj.get("tablename")+"");
        log.setTitle(newObj.get("anothername")+"");
        log.setAddress(ip);
        log.setDetail("删除了"+newObj.get("fieldname"));
        log.setCreateTime(new Date());
        log.setModifyTime(new Date());
        log.setModifyUser(name);
        log.setCrUser(name);
        super.insert(log);

    }
}
