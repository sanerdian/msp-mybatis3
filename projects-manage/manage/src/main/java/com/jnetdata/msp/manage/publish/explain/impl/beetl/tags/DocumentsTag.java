package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.DocumentsTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_DOCUMENTS)
public class DocumentsTag extends GeneralTag<DocumentsTagAttr> {

    @Resource
    PublishContentManager publishContentManager;


    @Override
    public void render() {
        try {
            PublishObj obj = this.getPublishObj();
            DocumentsTagAttr attr = this.getAttrs();
            //需要根据  attr.getNum() 的数量获取列表
            List<Map<String, Object>> docs = publishContentManager.getDocuments(obj, attr.getId());
            int i = 0;
            for (Map<String, Object> doc : docs) {
                i++;
                setContent(doc, TagsConstant.JNET_DOCUMENT);
                setContent(i, TagsConstant.JNET_ROWNUM);
                this.doBodyRender();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return TagsConstant.JNET_DOCUMENTS;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
