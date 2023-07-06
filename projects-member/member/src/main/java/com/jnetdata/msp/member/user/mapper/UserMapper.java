package com.jnetdata.msp.member.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.github.yulichang.base.MPJBaseMapper;
import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.member.user.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by TF on 2019/3/25.
 */
public interface UserMapper extends BaseMapper<User>{
    @Select("select MAX(INVITATION_CODE) from userinfo")
    Integer getYQM();

    Page<User> userPage(Page<User> page,@Param("user") User user);
}
