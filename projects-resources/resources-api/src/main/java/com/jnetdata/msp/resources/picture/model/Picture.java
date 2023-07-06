package com.jnetdata.msp.resources.picture.model;

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
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Data
@TableName("j_file")
@ApiModel(description = "图片配置")
public class Picture extends BaseEntity implements EntityId<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "ID", type = IdType.ID_WORKER)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField(value = "TYPE")
    @NotEmpty(message = "文件分类")
    @ApiModelProperty(value = "文件分类")
    private Integer type;

    @TableField(value = "NAME")
    @NotEmpty(message = "文件名")
    @ApiModelProperty(value = "文件名")
    private String name;

    @TableField(value = "FSIZE")
    @NotEmpty(message = "文件大小")
    @ApiModelProperty(value = "文件大小")
    private Double fsize;

    @TableField(value = "FORM")
    @NotEmpty(message = "文件格式")
    @ApiModelProperty(value = "文件格式")
    private String form;

    @TableField(value = "DATETIME")
    @NotEmpty(message = "上传时间")
    @ApiModelProperty(value = "上传时间")
    private Date datetime;

    @TableField(value = "PATH")
    @NotEmpty(message = "文件路径")
    @ApiModelProperty(value = "文件路径")
    private String path;

    @TableField(value = "RESC")
    @NotEmpty(message = "文件来源")
    @ApiModelProperty(value = "文件来源")
    private String resc;

    @TableField(value = "COPYRIGHT")
    @NotEmpty(message = "文件版权")
    @ApiModelProperty(value = "文件版权")
    private String copyright;

    @TableField(value = "FDESC")
    @NotEmpty(message = "文件描述")
    @ApiModelProperty(value = "文件描述")
    private String fdesc;

    @TableField(value = "SCALE")
    @NotEmpty(message = "文件比例")
    @ApiModelProperty(value = "文件比例")
    private String scale;

    @TableField(value = "RESO")
    @NotEmpty(message = "文件分辨率")
    @ApiModelProperty(value = "文件分辨率")
    private String reso;

    @TableField(value = "STYLE")
    @NotEmpty(message = "文件板式")
    @ApiModelProperty(value = "文件板式")
    private String style;

    @TableField(exist = false)
    @NotEmpty(message = "查询结束时间")
    @ApiModelProperty(value = "查询结束时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime;

    @TableField(exist = false)
    @NotEmpty(message = "查询开始时间")
    @ApiModelProperty(value = "查询开始时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date startTime;

    @TableField(value = "CORPORATION")
    @NotEmpty(message = "制作单位")
    @ApiModelProperty(value = "制作单位")
    private String corporation;

    @TableField(value = "CODESTYLE")
    @NotEmpty(message = "编码格式")
    @ApiModelProperty(value = "编码格式")
    private String codestyle ;

    @TableField(value = "RADIO")
    @NotEmpty(message = "音频声道")
    @ApiModelProperty(value = "音频声道")
    private String radio;

    @TableField(value = "SUPERVISOR")
    @NotEmpty(message = "监制")
    @ApiModelProperty(value = "监制")
    private String supervisor;

    @TableField(value = "CRUSER")
    @NotEmpty(message = "创建者")
    @ApiModelProperty(value = "创建者")
    private String crUser;

    @TableField(value = "PRODUCER")
    @NotEmpty(message = "制片")
    @ApiModelProperty(value = "制片")
    private String producer;

    @TableField(value = "PLANNER")
    @NotEmpty(message = "策划")
    @ApiModelProperty(value = "策划")
    private String planner;

    @TableField(value = "WRITER")
    @NotEmpty(message = "编导")
    @ApiModelProperty(value = "编导")
    private String writer;

    @TableField(value = "CAMERAMAN")
    @NotEmpty(message = "摄影")
    @ApiModelProperty(value = "摄影")
    private String cameraman;

    @TableField(value = "WORD")
    @NotEmpty(message = "字幕")
    @ApiModelProperty(value = "字幕")
    private String word;

    @TableField(value = "MUSIC")
    @NotEmpty(message = "配乐")
    @ApiModelProperty(value = "配乐")
    private String music;

    @TableField(value = "SIGN")
    @NotEmpty(message = "上传标识")
    @ApiModelProperty(value = "上传标识")
    private Integer sign;

    @TableField(value = "KEYWORD")
    @ApiModelProperty(value = "关键词")
    private String keyword;

    @TableField(value = "CUSTOM_KEYWORD")
    @ApiModelProperty(value = "自定义关键词")
    private String customKeyword;

    @TableField(value = "FREFERENCES")
    @NotEmpty(message = "参考文献")
    @ApiModelProperty(value = "参考文献")
    private String freferences;

    @TableField(value = "LENGTH")
    @NotEmpty(message = "音频时长")
    @ApiModelProperty(value = "音频时长")
    private String length;

    @TableField(value = "contentType")
    @NotEmpty(message = "内容类型")
    @ApiModelProperty(value = "内容类型")
    private String contentType;

    @TableField(value = "COLUMNID")
    @NotEmpty(message = "栏目id")
    @ApiModelProperty(value = "栏目id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long columnId;

    @TableField(value = "ISNEW")
    @NotEmpty(message = "是否为修改后的图片")
    @ApiModelProperty("是否为修改后的图片")
    private Integer isNew;

    @TableField(value = "OTHERFORM")
    @NotEmpty(message = "其他格式")
    @ApiModelProperty(value = "其他格式")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private String otherForm;

    @TableField(value = "OTHERCP")
    @NotEmpty(message = "其他版权")
    @ApiModelProperty(value = "其他版权")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private String otherCp;

    @TableField(value = "URL")
    @NotEmpty(message = "url")
    @ApiModelProperty(value = "访问路径")
    private String url;

    @TableField(value = "PICSIGN")
    @NotEmpty(message = "横竖图区别")
    @ApiModelProperty(value = "横竖图区别")
    private Integer picSign;

    @TableField(value = "PARENTID")
    @NotEmpty(message = "父栏目id")
    @ApiModelProperty(value = "父栏目id")
    private Long parentId;

    @TableField(value = "SITEID")
    @NotEmpty(message = "站点id")
    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @TableField(value = "COMPANYID")
    @NotEmpty(message = "机构id")
    @ApiModelProperty(value = "机构id")
    private Long companyId;

    @TableField(value = "STATUS")
    @NotEmpty(message = "资源状态")
    @ApiModelProperty(value = "资源状态")
    private Integer status;

    @TableField(value = "RESOTYPE")
    @NotEmpty(message = "分辨率")
    @ApiModelProperty("分辨率")
    private Integer resoType;

    @TableField(value = "COLLECTION")
    @NotEmpty(message = "收藏标识")
    @ApiModelProperty("收藏标识")
    private Integer collection;

    @TableField(value = "RESPARENTID")
    @NotEmpty(message = "父资源id")
    @ApiModelProperty("父资源id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long resParentId;

    @TableField(value = "PDFSIGN")
    @NotEmpty(message = "pdf标识")
    @ApiModelProperty("pdf标识")
    private Integer pdfSign;

    @TableField(value = "ISPROCESSED")
    @NotEmpty(message = "资源是否已处理")
    @ApiModelProperty("资源是否已处理")
    private Integer isProcessed;

    @TableField(value = "PDFURL")
    @NotEmpty(message = "pdfUri")
    @ApiModelProperty("pdfUrl")
    private String pdfUrl;

    @TableField(value = "CUTPICURL")
    @NotEmpty(message = "视频截图url")
    @ApiModelProperty("视频截图url")
    private String cutPicUrl;

    @TableField(value = "VIDEOURL")
    @NotEmpty(message = "视频加水印url")
    @ApiModelProperty("视频加水印url")
    private String videoUrl;

    @TableField(value = "H264URL")
    @NotEmpty(message = "视频H264url")
    @ApiModelProperty("视频H264url")
    private String h264Url;

    @TableField(value = "MP3URL")
    @NotEmpty(message = "mp3url")
    @ApiModelProperty("mp3url")
    private String mp3url;

    @TableField(value = "dataid")
    @ApiModelProperty("dataid")
    private String dataId;

    @TableField(value = "TABLEID")
    private String tableid;

    @TableField(value = "NEWVIDEOID")
    @NotEmpty(message = "加水印视频文件id")
    @ApiModelProperty("加水印视频文件id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long newvideoID;

    @TableField(value = "COVER")
    @NotEmpty(message = "视频封面")
    @ApiModelProperty("视频封面")
    private String cover ;

    @TableField(value = "PROCESSING")
    @NotEmpty(message = "资源正在处理标识")
    @ApiModelProperty("资源正在处理标识")
    private Integer processing;

    @TableField(value = "WIDTH")
    @NotEmpty(message = "宽度")
    @ApiModelProperty("宽度")
    private Integer width;

    @TableField(value = "HEIGHT")
    @NotEmpty(message = "高度")
    @ApiModelProperty("高度")
    private Integer height;

    @TableField(value = "THEME")
    @NotEmpty(message = "主题词")
    @ApiModelProperty("主题词")
    private String theme;

    @TableField(value = "TYPENAME")
    @NotEmpty(message = "分类")
    @ApiModelProperty("分类")
    private String typeName;

    @TableField(value = "LOCATION")
    @NotEmpty(message = "拍摄地点")
    @ApiModelProperty("拍摄地点")
    private String location;

    @TableField(value = "PHOTOTIME")
    @ApiModelProperty("拍摄时间")
    private Date photoTime;

    @TableField(value = "TAKE_TIME")
    @ApiModelProperty("拍摄时间")
    private String takeTime;

    @TableField(value = "LATITUDE")
    @ApiModelProperty("纬度")
    private String latitude;

    @TableField(value = "LONGITUDE")
    @ApiModelProperty("经度")
    private String longitude;

    @TableField(value = "PROTYPE")
    @NotEmpty(message = "项目附件类型")
    @ApiModelProperty("项目附件类型")
    private String proType;

    @TableField(value = "REGION")
    @NotEmpty(message = "地区")
    @ApiModelProperty("地区")
    private String region;

    @TableField(value = "INTODATABASE")
    @NotEmpty(message = "入库时间")
    @ApiModelProperty("入库时间")
    private Date intoDatabase;

    @TableField(value = "SHOTMAN")
    @NotEmpty(message = "拍摄者")
    @ApiModelProperty("拍摄者")
    private String shotman;

    @TableField(value = "INHERRITORPRO")
    @NotEmpty(message = "所属项目")
    @ApiModelProperty("所属项目")
    private String inherritorPro;

    @TableField(value = "INHERITOR")
    @NotEmpty(message = "所属传承人")
    @ApiModelProperty("所属传承人")
    private String inheritor;

    @TableField(exist = false)
    private String relationname;

    @TableField(exist = false)
    private String inheritname;

    @ApiModelProperty("图片信息")
    @TableField(value = "IMGINFO")
    private String imginfo;

    @ApiModelProperty("排序号")
    @TableField(value = "resourceorder")
    private Long resourceOrder;

    @TableField(value = "SAMPLERATE")
    @NotEmpty(message = "采样率")
    @ApiModelProperty(value = "采样率")
    private Integer sampleRate;

    @TableField(value = "AUDIONUM")
    @NotEmpty(message = "声道数")
    @ApiModelProperty(value = "声道数")
    private String audioNum;

    @TableField(value = "BITRATE")
    @NotEmpty(message = "比特率")
    @ApiModelProperty("比特率")
    private Integer bitRate;

    @TableField(value = "CPSPATH")
    @NotEmpty(message = "缩略图路径")
    @ApiModelProperty("缩略图路径")
    private String cpsPath;


    @Override
    protected Object clone() throws CloneNotSupportedException {

        return super.clone();
    }
}
