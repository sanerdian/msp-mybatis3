package com.jnetdata.msp.cs1.test_cy;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.data.api.EntityId;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName TestCy
 * @Description 陈闫的测试实体类
 * @Author chenyan
 * @Date 2023/6/2 16:37
 */
@Data
@TableName("jmeta_test_cy")
@ApiModel(value = "cy对象",description = "")
public class TestCy extends BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    //主键id
    @ApiModelProperty(value = "主键id")
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    private Long id;

    //英文名字
    @ApiModelProperty(value = "chineseName")
    @TableField("chinese_name")
    private String chineseName;

    //中文名字
    @ApiModelProperty(value = "englishName")
    @TableField("english_name")
    private String englishName;

    //球衣号码
    @ApiModelProperty(value = "uniform_number")
    @TableField("uniform_number")
    private Integer uniformNumber;

    //球队名称
    @ApiModelProperty(value = "teamName")
    @TableField("team_name")
    private String teamName;

    //年龄
    @ApiModelProperty(value = "age")
    @TableField("age")
    private Integer age;

    //性别
    @ApiModelProperty(value = "sex")
    @TableField("sex")
    private Integer sex;

    //身高
    @ApiModelProperty(value = "height")
    @TableField("height")
    private Double height;

    //体重
    @ApiModelProperty(value = "weight")
    @TableField("weight")
    private Double weight;

    //位置
    @ApiModelProperty(value = "position")
    @TableField("position")
    private String position;

    //国籍
    @ApiModelProperty(value = "nationality")
    @TableField("nationality")
    private String nationality;

    //薪资
    @ApiModelProperty(value = "salary")
    @TableField("salary")
    private Double salary;

    //生日
    @ApiModelProperty(value = "birthday")
    @TableField("birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    //选秀顺位
    @ApiModelProperty(value = "draftRanking")
    @TableField("draft_ranking")
    private String draftRanking;

    //臂展
    @ApiModelProperty(value = "wingspan")
    @TableField("wingspan")
    private Integer wingspan;

    //站立摸高
    @ApiModelProperty(value = "standingReach")
    @TableField("standing_reach")
    private Integer standingReach;

}
