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

@TableName(value = "dict_adjectelation")
public class DictAdjectelation {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "JMETAADJECTELATIONID", type = IdType.INPUT)
//    @TableField(value = "JMETAADJECTELATIONID")
    private BigInteger jmetaadjectelationid;

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

    @TableField(value = "WORDNAME")
    private String wordname;

    @TableField(value = "SOURCE")
    private String source;

    @TableField(value = "CATEGORY")
    private String category;

    @TableField(value = "STOPWORD")
    private Long stopword;

    /**
     * -1停用 0不生效 1生效
     */
    @TableField(value = "STATUS")
    private Integer status;

    @TableField(value = "SATISFIED")
    private String satisfied;

    @TableField(value = "AD_WORD")
    private String adWord;

    public static final String COL_JMETAADJECTELATIONID = "JMETAADJECTELATIONID";

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

    public static final String COL_WORDNAME = "WORDNAME";

    public static final String COL_SOURCE = "SOURCE";

    public static final String COL_CATEGORY = "CATEGORY";

    public static final String COL_STOPWORD = "STOPWORD";

    public static final String COL_STATUS = "STATUS";

    public static final String COL_SATISFIED = "SATISFIED";

    public static final String COL_AD_WORD = "AD_WORD";

    public BigInteger getJmetaadjectelationid() {
        return jmetaadjectelationid;
    }

    public void setJmetaadjectelationid(BigInteger jmetaadjectelationid) {
        this.jmetaadjectelationid = jmetaadjectelationid;
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
     * @return WORDNAME
     */
    public String getWordname() {
        return wordname;
    }

    /**
     * @param wordname
     */
    public void setWordname(String wordname) {
        this.wordname = wordname;
    }

    /**
     * @return SOURCE
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return CATEGORY
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return STOPWORD
     */
    public Long getStopword() {
        return stopword;
    }

    /**
     * @param stopword
     */
    public void setStopword(Long stopword) {
        this.stopword = stopword;
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
     * @return SATISFIED
     */
    public String getSatisfied() {
        return satisfied;
    }

    /**
     * @param satisfied
     */
    public void setSatisfied(String satisfied) {
        this.satisfied = satisfied;
    }

    /**
     * @return AD_WORD
     */
    public String getAdWord() {
        return adWord;
    }

    /**
     * @param adWord
     */
    public void setAdWord(String adWord) {
        this.adWord = adWord;
    }
}