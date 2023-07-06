package com.jnetdata.msp.metadata.gitaccount.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.jnetdata.msp.core.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.thenicesys.data.api.EntityId;


/**
 * <p>
 * 
 * </p>
 *
 * @author zyj
 * @since 2019-09-23
 */
@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@TableName("gitaccount")
@ApiModel(value="Gitaccount对象", description="git账号信息")
public class Gitaccount extends  BaseEntity implements EntityId<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @JSONField(serializeUsing = org.thenicesys.fastjson.serializer.ToStringSerializer.class)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    @ApiModelProperty(value = "git账号")
    @TableField("ACCOUNT")
    private String account;

    @ApiModelProperty(value = "密码")
    @TableField("PASSWORD")
    private String password;

    @ApiModelProperty(value = "邮箱")
    @TableField("EMAIL")
    private String email;

    @ApiModelProperty(value = "ssh密匙")
    @TableField("SSHKEY")
    private String sshkey;

    @ApiModelProperty(value = "git路径")
    @TableField("GITPATH")
    private String gitpath;

}
