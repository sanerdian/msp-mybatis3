package com.jnetdata.msp.core.model.util;

import org.thenicesys.data.api.EntityGetId;

import java.io.Serializable;

/**
 * 当前用户
 * @author Administrator
 */
public interface IUser<IdType extends Serializable> extends EntityGetId<IdType>{

    /**
     * 用户名(账号名)
     */
    String getName();

    /**
     * 用户昵称
     */
    String getNickName();

    String getTrueName();

    String getMobile();

}
