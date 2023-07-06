package com.jnetdata.msp.dict3.wordname.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.math.BigInteger;
import java.util.Date;

@TableName(value = "dict_changjing")
public class DictChangjing {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "JMETACHANGJINGID", type = IdType.INPUT)
//    @TableId(value = "JMETACHANGJINGID", type = IdType.INPUT)
    private BigInteger jmetachangjingid;

    @TableField(value = "CHANGJINGCI")
    private String changjingci;

    @TableField(value = "CTZT")
    private Boolean ctzt;

    @TableField(value = "SOURCE")
    private String source;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "CRTIME")
    private Date crtime;

    @TableField(value = "CRUSER")
    private String cruser;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "modify_time")
    private Date modifyTime;

    @TableField(value = "MODIFY_USER")
    private String modifyUser;

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }

    /**
     * -1停用 0不生效 1生效
     */
    @TableField(value = "STATUS")

    private Integer status;

    @TableField(value = "FENLEI")
    private String fenlei;
    public static final String COL_JMETACHANGJINGID = "JMETACHANGJINGID";

    public static final String COL_CHANGJINGCI = "CHANGJINGCI";

    public static final String COL_CTZT = "CTZT";

    public static final String COL_SOURCE = "SOURCE";

    public static final String COL_CRTIME = "CRTIME";

    public static final String COL_CRUSR = "CRUSR";

    public static final String COL_OPTIME = "OPTIME";

    public static final String COL_OPUSR = "OPUSR";

    public static final String COL_STATUS = "STATUS";

    public BigInteger getJmetachangjingid() {
        return jmetachangjingid;
    }

    public void setJmetachangjingid(BigInteger jmetachangjingid) {
        this.jmetachangjingid = jmetachangjingid;
    }

    public String getChangjingci() {
        return changjingci;
    }

    public void setChangjingci(String changjingci) {
        this.changjingci = changjingci;
    }

    public Boolean getCtzt() {
        return ctzt;
    }

    public void setCtzt(Boolean ctzt) {
        this.ctzt = ctzt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCrtime() {
        return crtime;
    }

    public void setCrtime(Date crtime) {
        this.crtime = crtime;
    }

    public String getCruser() {
        return cruser;
    }

    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Integer getStatus() {
        return status;
    }




    /**
     * 设置-1停用 0不生效 1生效
     *
     * @param status -1停用 0不生效 1生效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}