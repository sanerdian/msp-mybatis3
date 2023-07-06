package com.jnetdata.msp.manage.publish.explain.impl.beetl.tags;

import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.common.utils.PublishUtil;
import com.jnetdata.msp.manage.publish.core.publish.PublishContentManager;
import com.jnetdata.msp.manage.publish.explain.common.constant.TagsConstant;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.annoation.BeetlTagName;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.core.GeneralTag;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.MetadatasTagAttr;
/*import org.apache.commons.lang3.StringUtils;*/
/*import org.jsoup.Jsoup;*/
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by penghe.li
 */
@Service
@Scope("prototype")
@BeetlTagName(TagsConstant.JNET_METADATAS)
public class MetadatasTag extends GeneralTag<MetadatasTagAttr> {

    @Resource
    PublishContentManager publishContentManager;

    @Resource
    PublishUtil publishUtil;


    @Override
    public void render() {
        try {
            PublishObj publishObj = this.getPublishObj();
            MetadatasTagAttr attr = this.getAttrs();
            //需要根据  attr.getNum() 的数量获取列表
            Integer num = attr.getNum();
            Integer from = attr.getFrom();
            Integer size = attr.getPageSize();
            publishObj.setNum(num);
            publishObj.setFrom(from);
            publishObj.setOrder(attr.getOrder());

            long chnlId = publishUtil.readDocumentsChannelId(publishObj, attr.getId());
            List<Map<String, Object>> docList;
            if(attr.isAllChnl()){
                List<Long> cids = new ArrayList<>();
                publishUtil.readChildrenChannelByChannelId(chnlId,cids);
                String chnlIds = cids.stream().map(m -> m.toString()).collect(Collectors.joining(","));
                docList = publishContentManager.getPubList(publishObj, chnlIds);
            }else{
                if(StringUtils.isNotEmpty(attr.getChnlIds())){
                    List<String> list = new ArrayList<>();
                    for (String str : attr.getChnlIds().split(",")) {
                        list.add(publishUtil.readDocumentsChannelId(publishObj, str).toString());
                    }
                    docList = publishContentManager.getPubList(publishObj, String.join(",",list));
                }else{
                    docList = publishContentManager.getPubList(publishObj, chnlId);
                }
            }
            Map<String, Object> doc;
            int index = 0;
            int docsize = docList.size();
            if (num != null) {
                num = Math.min(docsize, num);
            } else {
                num = docsize;
            }
            for (int i = attr.getStartpos(); i < num; i++) {
                if(attr.isPage() && i%size == 0) this.ctx.byteWriter.writeString("\n<abc class='page"+(i/size+1)+"'>\n");
                doc = docList.get(i);
                doc.put(PublishConstants.PUBLISH_METADATA_FIELD_DOCCHANNELID, chnlId);
                setContent(doc, TagsConstant.JNET_METADATA);
                setContent((index + 1), TagsConstant.JNET_ROWNUM);
                index++;
                this.doBodyRender();
                if(attr.isPage() && ((i+1)%size == 0 || i==num-1)) this.ctx.byteWriter.writeString("\n</abc>\n");
            }

            if(attr.isPage()) this.ctx.byteWriter.writeString(getPageHtml(num,size));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPageHtml(int total,int size){
        int pageTotal = total/size;
        if(total%size != 0) pageTotal += 1;
        String pagehtml = "\n<div class='page'>\n" +
                "    <span class='page_total'>共"+pageTotal+"页,"+total+"条数据</span>\n" +
                "    <span class='page_size'>"+size+"条/页</span>\n"+
                "    <span class='page_item page_current'>1</span>\n";
        for (int i = 1; i < pageTotal; i++) {
            pagehtml += "    <span class='page_item'>"+(i+1)+"</span>\n";
        }
        pagehtml += "</div>\n" +
                "<script>\n" +
                "    $('abc').hide().first().show();\n" +
                "    $('.page_item').click(function(){\n" +
                "        $('abc').hide();\n" +
                "        $('.page'+$(this).text()).show();\n" +
                "        $('.page_current').removeClass('page_current');\n" +
                "        $(this).addClass('page_current');\n" +
                "    })\n" +
                "</script>\n";
        return pagehtml;
    }

    @Override
    public String getName() {
        return TagsConstant.JNET_METADATAS;
    }

    @Override
    public String getPathName() {
        return PublishConstants.TAG_SEPERATE + getName();
    }
}
