package com.jnetdata.msp.message.msg.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface MsgSelectMapper extends BaseMapper {

    @Select("select xwid,organizationid,organizationname,namee,sex,age,education,politicsstatus,tablename,treename,treeid,CRTIME,conditionn from t_network WHERE organizationid!=0")
    List<Map> selectall();

    @Select("select TRUENAME\n" +
            "from (\n" +
            "select TRUENAME,TIMESTAMPDIFF(YEAR,ba.birthdate,CURDATE()) age,sex,politicsstatus,educationbackground\n" +
            "from USERINFO as ba\n" +
            "where userid in(\n" +
            "select userid\n" +
            "from GRP_USER\n" +
            "where groupid=`#{s}`\n" +
            ")) a\n" +
            "where (CASE WHEN #{conditionn} = '小于'  then #{ageForBirthday} > a.age\n" +
            "WHEN #{conditionn} = '小于等于'  then #{ageForBirthday} >= a.age\n" +
            " WHEN #{conditionn} = '大于'  then #{ageForBirthday} < a.age\n" +
            " WHEN #{conditionn} = '大于等于'  then #{ageForBirthday} <= a.age\n" +
            "WHEN #{conditionn} = '等于'  then #{ageForBirthday} = a.age\n" +
            "WHEN #{conditionn} = '介于区间'  then #{ageForBirthday} <= a.age and #{ageBirthday}>=a.age\n" +
            "ELSE TRUE\n" +
            "END\n" +
            ")and \n" +
            "if(#{truename} is not null,a.TRUENAME like CONCAT(\"%\",#{truename},\"%\"),TRUE) and if(#{sex} is not null,a.sex=#{sex},TRUE) and \n" +
            "if(#{politicsstatus} is null,TRUE,a.politicsstatus= #{politicsstatus}) and if(#{educationbackground} is null,TRUE,a.educationbackground=#{educationbackground})")
    List<Map> selectalluser(@Param("s") Object s,@Param("conditionn") Object conditionn, @Param("ageForBirthday") Object ageForBirthday,@Param("ageBirthday") Object ageBirthday,@Param("truename") Object truename,@Param("sex") Object sex,@Param("politicsstatus") Object politicsstatus,@Param("educationbackground") Object educationbackground);

    @Select("select *\n" +
            "from t_network\n" +
            "where userid!=0")
    List<Map> selectuser();








}
