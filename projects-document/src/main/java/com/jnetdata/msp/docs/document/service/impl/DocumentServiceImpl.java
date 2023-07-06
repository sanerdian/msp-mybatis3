package com.jnetdata.msp.docs.document.service.impl;


import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.core.service.BaseServiceException;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.docs.document.mapper.DocumentMapper;
import com.jnetdata.msp.docs.document.model.Document;
import com.jnetdata.msp.docs.document.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 */
@Service
public class DocumentServiceImpl extends BaseServiceImpl<DocumentMapper, Document> implements DocumentService {

    @Override
    protected PropertyWrapper<Document> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("id")
                .eq("contentType")
                .like("description")
                .like("url")
                .getWrapper();
    }

    @Override
    public List<Map<String,String>> getFileAttributes(String ids) {
        if(ObjectUtils.isEmpty(ids)){
            throw new BaseServiceException();
        }
        List<String> strs = Arrays.asList(ids.split(","));
        List<Map<String,String>> mapList = new ArrayList<>();
        for (String str : strs){
            Document document = getById(Long.valueOf(str));
            if(ObjectUtils.isEmpty(document)){
                throw new BaseServiceException("没有资源");
            }
            Map<String,String> map =new HashMap<>();
            map.put("id",document.getId().toString());
            map.put("name",document.getName());
            map.put("description",document.getDescription());
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
            map.put("createTime",simpleDateFormat.format(document.getCreateTime()));
            mapList.add(map);
        }
        return mapList;
    }

}
