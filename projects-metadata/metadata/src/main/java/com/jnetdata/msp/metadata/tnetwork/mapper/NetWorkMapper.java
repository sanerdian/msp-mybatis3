package com.jnetdata.msp.metadata.tnetwork.mapper;

import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.metadata.tnetwork.model.NetWorkModel;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface NetWorkMapper extends BaseMapper<NetWorkModel> {

 /*   @Select("SELECT * FROM t_network WHERE ((conditionn = '小于' AND age > #{ageForBirthday}) OR (conditionn = '大于等于' AND age <= #{ageForBirthday})\n" +
            "OR (conditionn = '大于' AND AGE < #{ageForBirthday})  OR   (conditionn = '小于等于' AND  AGE >=#{ageForBirthday})\n" +
            "OR (conditionn = '等于' AND  AGE =#{ageForBirthday}) OR (conditionn = '介于区间' AND  AGE <= #{ageForBirthday} AND AGEONE >=#{ageForBirthday})OR (conditionn is null )\n" +
            ")and organizationid= #{s} and tablename= #{tablename} and (education =#{educationbackground} or (education is null) )\n" +
            "and(politicsstatus=#{politicsstatus} or (politicsstatus is null)) and ( (#{name} like '%'||namee||'%') or (namee is null))")*/
    List<NetWorkModel> selectOrganization(@Param("s") Long s, @Param("ageForBirthday") int ageForBirthday,@Param("educationbackground") String educationbackground,@Param("politicsstatus") String politicsstatus,@Param("name") String name,@Param("tablename") String tablename);

    @Select("select * from #{s} where id in #{set1}")
    List<Map> selectall(@Param("s") Long s, @Param("set1")Set<Long> set1);

}
