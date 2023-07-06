package com.jnetdata.msp.message.msg.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.message.msg.entity.MsgVoEntity;
import com.jnetdata.msp.message.msg.model.Msg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Created by TF on 2019/3/14.
 */
public interface MsgMapper extends BaseMapper<Msg> {
   @Update("update customernewslog set status = 1,MODIFY_USER='admin'  where addressee = #{userId}")
   boolean setAllRead(Long userId);

   @Select("SELECT id\n" +
           "from t_network\n" +
           "where ( ((\n" +
           "\t\t\t\t\tconditionn = '小于' \n" +
           "\t\t\t\t\tAND age > 20 \n" +
           "\t\t\t\t\t) \n" +
           "\t\t\t\tOR ( conditionn = '大于等于' AND age <= #{ageForBirthday} ) \n" +
           "\t\t\t\tOR ( conditionn = '大于' AND AGE < #{ageForBirthday} ) \n" +
           "\t\t\t\tOR ( conditionn = '小于等于' AND AGE >= #{ageForBirthday} ) \n" +
           "\t\t\t\tOR ( conditionn = '等于' AND AGE = #{ageForBirthday} ) \n" +
           "\t\t\t\tOR ( conditionn = '介于区间' AND AGE <= #{ageForBirthday} AND AGEONE >= #{ageForBirthday} ) \n" +
           "\t\t\tOR ( conditionn IS NULL )) \n" +
           "\t\t\tAND organizationid in(${s1})\n" +
           "\t\t\tAND ( education = #{educationbackground} OR ( education IS NULL ) ) \n" +
           "\t\t\tAND (\n" +
           "\t\t\t\tpoliticsstatus = #{politicsstatus} \n" +
           "\t\t\tOR ( politicsstatus IS NULL )) \n" +
           "\t\t\tAND (\n" +
           "\t\t\t\t( #{name} LIKE '%' || namee || '%' ) \n" +
           "\t\t\tOR ( namee IS NULL ))) or\tuserid =#{s}")
   List<Long> selectalluser(@Param("s") Long s,@Param("s1") String s1, @Param("educationbackground") String educationbackground,@Param("politicsstatus") String politicsstatus,@Param("ageForBirthday") int ageForBirthday,@Param("name")
           String name);

   Page<Msg> Userpagename(Page<Msg> page,@Param("msgVoEntity") MsgVoEntity msgVoEntity);

   List<Msg> selectcount(@Param("msgVoEntity") MsgVoEntity msgVoEntity);

   List<Msg> getInform(@Param("msgVoEntity") MsgVoEntity msgVoEntity);
   List<Msg> getInformm(@Param("msgVoEntity") MsgVoEntity msgVoEntity);

   @Select("\t\tselect fieldname\n" +
           "\t\t\t\tfrom dbfieldinfo\n" +
           "\t\t\t\twhere ISTITLE=1 and TABLENAME= #{s} ")
   String getTheTitleTield(@Param("s") String s);

   @Select("select ${name} from ${tablename} where id =#{id}")
   String getTheTitle(@Param("tablename") String tablename,@Param("name") String name ,@Param("id") String id);
}
