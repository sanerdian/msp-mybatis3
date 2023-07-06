package com.jnetdata.msp.dict3.wordname.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.math.BigInteger;
import java.util.Date;

@TableName(value = "dict_stopword")
public class DictStopword {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "JMETASTOPWORDID", type = IdType.INPUT)
    @TableField(value = "JMETASTOPWORDID")
    private BigInteger jmetastopwordid;

    @TableField(value = "CRUSER")
    private String cruser;

    public String getWordname() {
        return wordname;
    }

    public void setWordname(String wordname) {
        this.wordname = wordname;
    }

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "CRTIME")

    private Date crtime;

    @TableField(value = "WORDNAME")
    private String wordname;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField(value = "WORDID")
    private Long wordid;

    /**
     * -1停用 0不生效 1生效
     */
    @TableField(value = "STATUS")
    private Integer status;

    @TableField(value = "OPERUSER")
    private String operuser;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "OPERTIME")
    private Date opertime;

    @TableField(value = "STOPWORDNAME")
    private String stopwordname;

    @TableField(value = "TYPE")
    private Integer type;

    @TableField(value = "DOCSTATUS")
    private Integer docstatus;

    @TableField(value = "SOURCE")
    private Integer source;

    @TableField(value = "DOCTITLE")
    private String doctitle;

    @TableField(value = "FIRST")
    private String first;

    @TableField(value = "SECOND")
    private String second;

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }

    @TableField(value = "LAIYUAN")
    private String laiyuan;

    @TableField(value = "FENLEI")
    private String fenlei;

    public static final String COL_JMETASTOPWORDID = "JMETASTOPWORDID";

    public static final String COL_CRUSER = "CRUSER";

    public static final String COL_CRTIME = "CRTIME";

    public static final String COL_STATUS = "STATUS";

    public static final String COL_OPERUSER = "OPERUSER";

    public static final String COL_OPERTIME = "OPERTIME";

    public static final String COL_STOPWORDNAME = "STOPWORDNAME";

    public static final String COL_TYPE = "TYPE";

    public static final String COL_DOCSTATUS = "DOCSTATUS";

    public static final String COL_SOURCE = "SOURCE";

    public static final String COL_DOCTITLE = "DOCTITLE";

    public static final String COL_FIRST = "FIRST";

    public static final String COL_SECOND = "SECOND";

    public static final String COL_LAIYUAN = "LAIYUAN";

    public BigInteger getJmetastopwordid() {
        return jmetastopwordid;
    }

    public void setJmetastopwordid(BigInteger jmetastopwordid) {
        this.jmetastopwordid = jmetastopwordid;
    }

    public Long getWordid() {
        return wordid;
    }

    public void setWordid(Long wordid) {
        this.wordid = wordid;
    }

   /*
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
     * @return STOPWORDNAME
     */
    public String getStopwordname() {
        return stopwordname;
    }

    /**
     * @param stopwordname
     */
    public void setStopwordname(String stopwordname) {
        this.stopwordname = stopwordname;
    }

    /**
     * @return TYPE
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
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
     * @return SOURCE
     */
    public Integer getSource() {
        return source;
    }

    /**
     * @param source
     */
    public void setSource(Integer source) {
        this.source = source;
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
     * @return FIRST
     */
    public String getFirst() {
        return first;
    }

    /**
     * @param first
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * @return SECOND
     */
    public String getSecond() {
        return second;
    }

    /**
     * @param second
     */
    public void setSecond(String second) {
        this.second = second;
    }

    /**
     * @return LAIYUAN
     */
    public String getLaiyuan() {
        return laiyuan;
    }

    /**
     * @param laiyuan
     */
    public void setLaiyuan(String laiyuan) {
        this.laiyuan = laiyuan;
    }
}