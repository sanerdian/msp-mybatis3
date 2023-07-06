package com.jnetdata.msp.log.usermenu.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.log.usermenu.model.UserMenuLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * Created by TF on 2019/3/13.
 */
public interface UserMenuLogMapper extends BaseMapper<UserMenuLog> {

    @Select("SELECT `NAME` FROM `menuman`\n" +
            "where URL =#{name}")
    String selectsql(@Param("name") String name);
}
