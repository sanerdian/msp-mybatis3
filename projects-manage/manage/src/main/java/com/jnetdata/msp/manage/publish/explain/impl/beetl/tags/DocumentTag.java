package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import cn.hutool.json.JSONUtil;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.DocumentTagAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_DOCUMENT)
public class DocumentTag extends GeneralTag<DocumentTagAttr> {
    /*@Autowired
    PublishContentManager publishContentManager;*/

    @Override
    public void render() {
        System.out.println("document:" + this.getPathKey());
        System.out.println("tag:" + JSONUtil.toJsonStr(this.ctx.getCurrentTag()));
        System.out.println("ResourceId:" + this.ctx.getResourceId());
        /*try {
            PublishObj obj = this.getPublishObj();
            DocumentTagAttr documentTagAttr = this.getAttrs();
            //优先使用recid指定的情况
            if (documentTagAttr.getRecid() > 0) {
                Document doc = publishContentManager.getDocument(obj.getMonitorId(), documentTagAttr.getRecid());
                warpRender(doc, documentTagAttr);
            } else {
                Document doc = getContent();
                warpRender(doc, documentTagAttr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /*public void warpRender(Document doc, DocumentTagAttr attr) throws Exception {
        StringBuffer sb = new StringBuffer();
        if (attr.isAutolink()) {
            sb.append("<a title='" + doc.getDocTitle() + "' href='" + doc.getDocPubUrl() + "' target='" + attr.getTarget() + "'>");
            sb.append(getShowText(doc, attr));
            sb.append("</a>");
        } else {
            sb.append(getShowText(doc, attr));
        }
        this.ctx.byteWriter.writeString(sb.toString());
        this.doBodyRender();
    }*/

    /**
     * 根据模板的字段 展示文档的字段内容
     *
     * @param doc
     * @param attr
     * @return
     */
    /*private String getShowText(Document doc, DocumentTagAttr attr) {
        String text = "";
        boolean subStr = true;
        if (attr.getField().equalsIgnoreCase("doctitle")) {
            text = doc.getDocTitle();
        } else if (attr.getField().equalsIgnoreCase("subDocTitle")) {
            text = doc.getSubDocTitle();
        } else if (attr.getField().equalsIgnoreCase("docPeople")) {
            subStr = false;
            text = doc.getDocPeople();
        } else if (attr.getField().equalsIgnoreCase("docHtmlCon")) {
            subStr = false;
            text = doc.getDocHtmlCon();
        } else if (attr.getField().equalsIgnoreCase("docAbstract")) {
            text = doc.getDocAbstract();
        } else if (attr.getField().equalsIgnoreCase("docContent")) {
            subStr = false;
            text = doc.getDocContent();
        } else if (attr.getField().equalsIgnoreCase("crtime")) {
            subStr = false;
            text = TagsUtils.getDate(doc.getCrTime(), attr.getDateformat());
        }
        if (subStr && attr.getLength() > 0 && text.length() > attr.getLength()) {
            return text.substring(0, attr.getLength()).concat(attr.getTruncatedflag());
        }
        return text;
    }*/
    @Override
    public String getName() {
        return TagsConstant.JNET_DOCUMENT;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
