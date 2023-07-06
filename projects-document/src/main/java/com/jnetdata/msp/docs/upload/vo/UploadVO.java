package com.jnetdata.msp.docs.upload.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: ZKJW
 * @date: 2019/5/9
 */
@Data
@ApiModel
public class UploadVO {

    @ApiModelProperty(value = "上传文件")
    private MultipartFile file;

    @ApiModelProperty(value = "文件上传类型",notes = "目前仅包括承诺函、基础承诺函")
    private String uploadType;
}
