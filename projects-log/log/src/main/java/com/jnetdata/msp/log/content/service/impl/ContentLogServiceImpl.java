package com.jnetdata.msp.log.content.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.log.content.mapper.ContentLogMapper;
import com.jnetdata.msp.log.content.model.ContentLog;
import com.jnetdata.msp.log.content.service.ContentLogService;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
@Service
public class ContentLogServiceImpl extends BaseServiceImpl<ContentLogMapper, ContentLog> implements ContentLogService {

    @Override
    protected PropertyWrapper<ContentLog> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("columnid")
                .eq("siteid")
                .like("content")
                .like("handleType")
                .like("contentType")
                .like("detail")
                .between("createTime","endTime")
                .like("crUser")
                .getWrapper();
    }

    @Override
    public void addLog(String handleType,String contentType,String content,Boolean result) {
//        val user = SessionUser.getCurrentUser();
        ContentLog contentLog = new ContentLog();
        contentLog.setHandleType(handleType);
        contentLog.setContentType(contentType);
        contentLog.setContent(content);
        contentLog.setResult(result?"成功":"失败");
        super.insert(contentLog);
    }
}
