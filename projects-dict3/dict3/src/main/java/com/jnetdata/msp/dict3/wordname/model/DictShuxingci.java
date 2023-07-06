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

@TableName(value = "dict_shuxingci")
public class DictShuxingci {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "JMETASHUXINGCIID", type = IdType.INPUT)
//    @TableField(value = "JMETASHUXINGCIID")
    private BigInteger jmetashuxingciid;

    @TableField(value = "CRUSER")
    private String cruser;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "CRTIME")
    private Date crtime;

    @TableField(value = "CRNUMBER")
    private Integer crnumber;

    @TableField(value = "DOCCHANNELID")
    private Integer docchannelid;

    @TableField(value = "DOCSTATUS")
    private Integer docstatus;

    @TableField(value = "SINGLETEMPKATE")
    private Integer singletempkate;

    @TableField(value = "SITEID")
    private Integer siteid;

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
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField(value = "WORDID")
    private Long wordid;

    @TableField(value = "DOCRELTIME")
    private Date docreltime;

    @TableField(value = "SHUXINGCI")
    private String shuxingci;

    @TableField(value = "WORDNAME")
    private String wordname;

    @TableField(value = "RELATIONWORD")
    private String relationword;

    @TableField(value = "SX_RELATIONWORD")
    private String sxRelationword;

    @TableField(value = "TONGYICI")
    private String tongyici;

    @TableField(value = "PINYIN")
    private String pinyin;

    @TableField(value = "TONGYINCI")
    private String tongyinci;

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }

    @TableField(value = "RANK")
    private String rank;

    @TableField(value = "FENLEI")
    private String fenlei;
    /**
     * -1停用 0不生效 1生效
     */
    @TableField(value = "STATUS")
    private Integer status;

    public static final String COL_JMETASHUXINGCIID = "JMETASHUXINGCIID";

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

    public static final String COL_SHUXINGCI = "SHUXINGCI";

    public static final String COL_WORDNAME = "WORDNAME";

    public static final String COL_RELATIONWORD = "RELATIONWORD";

    public static final String COL_SX_RELATIONWORD = "SX_RELATIONWORD";

    public static final String COL_TONGYICI = "TONGYICI";

    public static final String COL_PINYIN = "PINYIN";

    public static final String COL_TONGYINCI = "TONGYINCI";

    public static final String COL_RANK = "RANK";

    public static final String COL_STATUS = "STATUS";

    public BigInteger getJmetashuxingciid() {
        return jmetashuxingciid;
    }

    public void setJmetashuxingciid(BigInteger jmetashuxingciid) {
        this.jmetashuxingciid = jmetashuxingciid;
    }

    public Long getWordid() {
        return wordid;
    }

    public void setWordid(Long wordid) {
        this.wordid = wordid;
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
    public Integer getCrnumber() {
        return crnumber;
    }

    /**
     * @param crnumber
     */
    public void setCrnumber(Integer crnumber) {
        this.crnumber = crnumber;
    }

    /**
     * @return DOCCHANNELID
     */
    public Integer getDocchannelid() {
        return docchannelid;
    }

    /**
     * @param docchannelid
     */
    public void setDocchannelid(Integer docchannelid) {
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
    public Integer getSingletempkate() {
        return singletempkate;
    }

    /**
     * @param singletempkate
     */
    public void setSingletempkate(Integer singletempkate) {
        this.singletempkate = singletempkate;
    }

    /**
     * @return SITEID
     */
    public Integer getSiteid() {
        return siteid;
    }

    /**
     * @param siteid
     */
    public void setSiteid(Integer siteid) {
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
     * @return SHUXINGCI
     */
    public String getShuxingci() {
        return shuxingci;
    }

    /**
     * @param shuxingci
     */
    public void setShuxingci(String shuxingci) {
        this.shuxingci = shuxingci;
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
     * @return RELATIONWORD
     */
    public String getRelationword() {
        return relationword;
    }

    /**
     * @param relationword
     */
    public void setRelationword(String relationword) {
        this.relationword = relationword;
    }

    /**
     * @return SX_RELATIONWORD
     */
    public String getSxRelationword() {
        return sxRelationword;
    }

    /**
     * @param sxRelationword
     */
    public void setSxRelationword(String sxRelationword) {
        this.sxRelationword = sxRelationword;
    }

    /**
     * @return TONGYICI
     */
    public String getTongyici() {
        return tongyici;
    }

    /**
     * @param tongyici
     */
    public void setTongyici(String tongyici) {
        this.tongyici = tongyici;
    }

    /**
     * @return PINYIN
     */
    public String getPinyin() {
        return pinyin;
    }

    /**
     * @param pinyin
     */
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    /**
     * @return TONGYINCI
     */
    public String getTongyinci() {
        return tongyinci;
    }

    /**
     * @param tongyinci
     */
    public void setTongyinci(String tongyinci) {
        this.tongyinci = tongyinci;
    }

    /**
     * @return RANK
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param rank
     */
    public void setRank(String rank) {
        this.rank = rank;
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