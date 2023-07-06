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

@TableName(value = "dict_fanyici")
public class DictFanyici {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "JMETAFANYICIID", type = IdType.INPUT)
//    @TableField(value = "JMETAFANYICIID")
    private BigInteger jmetafanyiciid;

    @TableField(value = "CRUSER")
    private String cruser;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "CRTIME")
    private Date crtime;

    @TableField(value = "CRNUMBER")
    private Long crnumber;

    @TableField(value = "DOCCHANNELID")
    private Long docchannelid;

    @TableField(value = "DOCSTATUS")
    private Integer docstatus;

    @TableField(value = "SINGLETEMPKATE")
    private Long singletempkate;

    @TableField(value = "SITEID")
    private Long siteid;

    @TableField(value = "DOCVALID")
    private Date docvalid;

    @TableField(value = "DOCPUBTIME")
    private Date docpubtime;

    @TableField(value = "OPERUSER")
    private String operuser;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "OPERTIME")
    private Date opertime;

    @TableField(value = "DOCTITLE")
    private String doctitle;

    @TableField(value = "DOCRELTIME")
    private Date docreltime;

    @TableField(value = "FANYICI")
    private String fanyici;

    @TableField(value = "XINGRONGCI")
    private String xingrongci;

    /**
     * -1停用 0不生效 1生效
     */
    @TableField(value = "STATUS")
    private Integer status;

    public static final String COL_JMETAFANYICIID = "JMETAFANYICIID";

    public static final String COL_CRUSER = "CRUSER";

    public static final String COL_CRTIME = "CRTIME";

    public static final String COL_CRNUMBER = "CRNUMBER";

    public static final String COL_DOCCHANNELID = "DOCCHANNELID";

    public static final String COL_DOCSTATUS = "DOCSTATUS";

    public static final String COL_SINGLETEMPKATE = "SINGLETEMPKATE";

    public static final String COL_SITEID = "SITEID";

    public static final String COL_DOCVALID = "DOCVALID";

    public static final String COL_DOCPUBTIME = "DOCPUBTIME";

    public static final String COL_OPERUSER = "OPERUSER";

    public static final String COL_OPERTIME = "OPERTIME";

    public static final String COL_DOCTITLE = "DOCTITLE";

    public static final String COL_DOCRELTIME = "DOCRELTIME";

    public static final String COL_FANYICI = "FANYICI";

    public static final String COL_XINGRONGCI = "XINGRONGCI";

    public static final String COL_STATUS = "STATUS";

    public BigInteger getJmetafanyiciid() {
        return jmetafanyiciid;
    }

    public void setJmetafanyiciid(BigInteger jmetafanyiciid) {
        this.jmetafanyiciid = jmetafanyiciid;
    }

    /**
     * @return CRUSER
     */
    public String getCruser() {
        return cruser;
    }

    /**
     * @param cruser
     */
    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    /**
     * @return CRTIME
     */
    public Date getCrtime() {
        return crtime;
    }

    /**
     * @param crtime
     */
    public void setCrtime(Date crtime) {
        this.crtime = crtime;
    }

    /**
     * @return CRNUMBER
     */
    public Long getCrnumber() {
        return crnumber;
    }

    /**
     * @param crnumber
     */
    public void setCrnumber(Long crnumber) {
        this.crnumber = crnumber;
    }

    /**
     * @return DOCCHANNELID
     */
    public Long getDocchannelid() {
        return docchannelid;
    }

    /**
     * @param docchannelid
     */
    public void setDocchannelid(Long docchannelid) {
        this.docchannelid = docchannelid;
    }

    /**
     * @return DOCSTATUS
     */
    public Integer getDocstatus() {
        return docstatus;
    }

    /**
     * @param docstatus
     */
    public void setDocstatus(Integer docstatus) {
        this.docstatus = docstatus;
    }

    /**
     * @return SINGLETEMPKATE
     */
    public Long getSingletempkate() {
        return singletempkate;
    }

    /**
     * @param singletempkate
     */
    public void setSingletempkate(Long singletempkate) {
        this.singletempkate = singletempkate;
    }

    /**
     * @return SITEID
     */
    public Long getSiteid() {
        return siteid;
    }

    /**
     * @param siteid
     */
    public void setSiteid(Long siteid) {
        this.siteid = siteid;
    }

    /**
     * @return DOCVALID
     */
    public Date getDocvalid() {
        return docvalid;
    }

    /**
     * @param docvalid
     */
    public void setDocvalid(Date docvalid) {
        this.docvalid = docvalid;
    }

    /**
     * @return DOCPUBTIME
     */
    public Date getDocpubtime() {
        return docpubtime;
    }

    /**
     * @param docpubtime
     */
    public void setDocpubtime(Date docpubtime) {
        this.docpubtime = docpubtime;
    }

    /**
     * @return OPERUSER
     */
    public String getOperuser() {
        return operuser;
    }

    /**
     * @param operuser
     */
    public void setOperuser(String operuser) {
        this.operuser = operuser;
    }

    /**
     * @return OPERTIME
     */
    public Date getOpertime() {
        return opertime;
    }

    /**
     * @param opertime
     */
    public void setOpertime(Date opertime) {
        this.opertime = opertime;
    }

    /**
     * @return DOCTITLE
     */
    public String getDoctitle() {
        return doctitle;
    }

    /**
     * @param doctitle
     */
    public void setDoctitle(String doctitle) {
        this.doctitle = doctitle;
    }

    /**
     * @return DOCRELTIME
     */
    public Date getDocreltime() {
        return docreltime;
    }

    /**
     * @param docreltime
     */
    public void setDocreltime(Date docreltime) {
        this.docreltime = docreltime;
    }

    /**
     * @return FANYICI
     */
    public String getFanyici() {
        return fanyici;
    }

    /**
     * @param fanyici
     */
    public void setFanyici(String fanyici) {
        this.fanyici = fanyici;
    }

    /**
     * @return XINGRONGCI
     */
    public String getXingrongci() {
        return xingrongci;
    }

    /**
     * @param xingrongci
     */
    public void setXingrongci(String xingrongci) {
        this.xingrongci = xingrongci;
    }

    /**
     * 获取-1停用 0不生效 1生效
     *
     * @return STATUS - -1停用 0不生效 1生效
     */
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