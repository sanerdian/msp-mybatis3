package com.jnetdata.msp.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jnetdata.msp.core.model.util.IUser;
import org.apache.ibatis.reflection.MetaObject;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Administrator on 2018/9/25.
 */
public class BaseMetaObjectHandler<IdType extends Serializable> implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        IUser<IdType> user = getCurrentUser();

        Date nowDate = getNowDate();

        doInsertFill(metaObject, user, nowDate);
        doUpdateFill(metaObject, user, nowDate);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        doUpdateFill(metaObject, getCurrentUser(), getNowDate());
    }

    protected IUser<IdType> getCurrentUser() {
        return null;
    }

    protected Date getNowDate() {
        return new Date();
    }

    private void doInsertFill(MetaObject metaObject, IUser<IdType> user, Date nowDate) {
        Object createTime = getFieldValByName("crtime", metaObject);
        Object docstatus = getFieldValByName("docstatus", metaObject);
        Object status = getFieldValByName("status", metaObject);
        if (null == createTime) {
            setFieldValByName("createTime", nowDate, metaObject);
        }
        if (null == docstatus) {
            setFieldValByName("docstatus", 0, metaObject);
        }
        if (null == status) {
            if(status instanceof Integer)
                setFieldValByName("status", 0, metaObject);
            else if(status instanceof String)
                setFieldValByName("status", "0", metaObject);

        }
        if (null==user) return;

        Object createBy = getFieldValByName("createBy", metaObject);
        Object crUser = getFieldValByName("crUser", metaObject);
        if (null == createBy) {
            setFieldValByName("createBy", user.getId(), metaObject);
        }
        if (Objects.isNull(crUser)) {
           this.setFieldValByName("crUser", user.getName(), metaObject);
        }
    }

    /**
     * 更新文件路径
     * @param metaObject
     * @param user
     * @param nowDate
     * @author hongshou
     * @date 2020/5/26
     */
    private void doUpdateFill(MetaObject metaObject, IUser<IdType> user, Date nowDate) {
        setFieldValByName("modifyTime", nowDate, metaObject);

        if (null==user) return;

        setFieldValByName("modifyBy", user.getId(), metaObject);
        setFieldValByName("modifyUser", user.getName(), metaObject);
    }

}
