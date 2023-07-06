package com.jnetdata.msp.manage.site.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jnetdata.msp.member.companyinfo.model.Companyinfo;
import com.jnetdata.msp.manage.site.model.Site;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CompanySiteInfo extends Companyinfo {

    @TableField(exist = false)
    @NotEmpty(message = "子节点集合")
    @ApiModelProperty(value = "子节点集合")
    private List<Site> children;

}
