package com.jnetdata.msp.member.menu.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.member.menu.model.Menu;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by TF on 2019/3/19.
 */
public interface MenuMapper extends BaseMapper<Menu> {
    @Update("update menuman set seq = seq+1 where parentid = #{parentId} and seq >= #{seq}")
    void updataSeq(Menu entity);

    @Select("SELECT DISTINCT REALM_NAME realmName,MODSTR modstr FROM MENUMAN WHERE MODSTR IS NOT NULL")
    List<Menu> getPerfix();
}
