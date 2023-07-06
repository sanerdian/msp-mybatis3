package com.jnetdata.msp.manage.publish.explain.impl.beetl.config;

import org.beetl.core.tag.HTMLTagSupportWrapper;
import org.beetl.core.tag.Tag;
import org.beetl.core.tag.TagFactory;

/**
 * @author penghe.li
 * @version 1.0
 * @description: TODO
 * @date 2022/9/1 11:23
 */
public class HTMLNestTagSupportWrapper extends HTMLTagSupportWrapper {
    Tag parentTag;

    @Override
    public void render() {
        if (args.length == 0 || args.length > 2) {
            throw new RuntimeException("参数错误，期望child,Map .....");
        }
        //标签名称
        String child = (String) args[0];
        // 首先查找 已经注册的Tag
        TagFactory tagFactory = null;
        String functionTagName = child.replace(':', '.');
        tagFactory = this.gt.getTagFactory(functionTagName);
        if (tagFactory == null) {
            //模板实现html标签
            String path = getHtmlTagResourceId(child);
            callHtmlTag(path);

        } else {
            //标签函数实现html标签
            callTag(tagFactory);
        }
    }

    @Override
    protected void callTag(TagFactory tagFactory) {

        Tag tag = tagFactory.createTag();
        tag.init(ctx, args, bs);

        //LPH新增
        Tag parentTag = tag.getParent();
        if (parentTag != null) {
            this.parentTag = parentTag;
        }

        tag.render();
        tag.afterRender();

    }

    public void afterRender() {
        //ctx.setCurrentTag(parent);

        //LPH修改
        if (parentTag != null) {
            this.parent = parentTag;
            ctx.setCurrentTag(parentTag);
        } else {
            ctx.setCurrentTag(parent);
        }
    }
}
