package com.jnetdata.msp.manage.publish.explain.impl.beetl.core;

import com.google.common.base.Preconditions;
import com.jnetdata.msp.constants.PublishConstants;
import com.jnetdata.msp.core.model.PublishObj;
import com.jnetdata.msp.manage.publish.core.common.utils.BeetlUtils;
import com.jnetdata.msp.manage.publish.explain.impl.beetl.tags.attr.base.TagBaseAttr;
import org.beetl.core.Context;
import org.beetl.core.statement.Statement;
import org.beetl.core.tag.GeneralVarTagBinding;
import org.beetl.core.tag.Tag;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class GeneralTag<T extends TagBaseAttr> extends GeneralVarTagBinding {

    private String pathKey = "";

    public String getPathKey() {
        return pathKey;
    }

    public abstract String getName();

    public abstract String getPathName();

    @Override
    public void init(Context ctx, Object[] args, Statement st) {
        super.init(ctx, args, st);
        Tag parentTag = this.getParent();
        if (parentTag != null) {
            GeneralTag generalTag = (GeneralTag) parentTag;
            this.pathKey = generalTag.getPathKey() + getPathName();
        } else {
            this.pathKey = getPathName();
        }
    }


    /**
     * 设置传递到子标签的值
     *
     * @param object
     */
    public void setContent(Object object, String name) {
        this.ctx.set(pathKey + PublishConstants.TAG_SEPERATE + name, object);
    }

    /**
     * 父栏目获取当前存储的值
     */
    /*public <E> E getContent(String name) {
        return (E) this.ctx.getGlobal(pathKey + name);
    }*/

    /**
     * 获取从父栏目传递过来的值
     */
    public <E> E getContent() {
        return (E) this.ctx.getGlobal(pathKey);
    }

    private T t;
    private Class<T> classType;

    public PublishObj getPublishObj() {
        Preconditions.checkArgument(this.ctx.getGlobal(PublishConstants.PUBLISH_OBJ_STR) != null, "请设置PublishObj对象");
        return (PublishObj) this.ctx.getGlobal(PublishConstants.PUBLISH_OBJ_STR);
    }

    /*public <T> T getAttrs() {
        if (null != t) {
            return (T) t;
        }
        t = BeanMapUtil.mapToBean(this.getAttributes(), getClassType());
        return (T) t;
    }*/

    public <T> T getAttrs() {
        if (null != t) {
            return (T) t;
        }
        Map<String, Object> attributes;
        try{
            attributes = this.getAttributes();
        }catch (ArrayIndexOutOfBoundsException e){
            attributes = new HashMap<>();
        }
        t = BeetlUtils.toObj(getClassType(), attributes);
        return (T) t;
    }

    private T getClassType() {
        try {
            return classType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.classType = (Class<T>) pt.getActualTypeArguments()[0];
    }
}
