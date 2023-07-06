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

@TableName(value = "dict_word")
public class DictWord {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "JMETASEARWORDID", type = IdType.INPUT)
    private BigInteger jmetasearwordid;

    @TableField(value = "CRUSER")
    private String cruser;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "CRTIME")
    private Date crtime;

    @TableField(value = "OPERUSER")
    private String operuser;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "OPERTIME")
    private Date opertime;

    @TableField(value = "WORDNAME")
    private String wordname;

    @TableField(value = "BZ")
    private String bz;

    /**
     * -1停用  0未生效 1 生效
     */
    @TableField(value = "STATUS")
    private Integer status;


    @TableField(value = "CATEGORY")
    private String category;

    @TableField(value = "SOURCE")
    private String source;

    @TableField(value = "FILTE")
    private Integer filte;

    @TableField(value = "DOCSTATUS")
    private Integer docstatus;

    @TableField(value = "SEARCH_ORDER")
    private String searchOrder;

    @TableField(value = "RECOMMEND")
    private Integer recommend;

    @TableField(value = "STOPWORD")
    private String stopword;

    @TableField(value = "DOCTITLE")
    private String doctitle;

    @TableField(value = "CXFL")
    private String cxfl;

    @TableField(value = "CTFL")
    private String ctfl;

    @TableField(value = "TONGYICI")
    private String tongyici;

    @TableField(value = "ENTONGYICI")
    private String entongyici;

    @TableField(value = "SUOXIE")
    private String suoxie;

    @TableField(value = "PINYIN")
    private String pinyin;

    @TableField(value = "TONGYINCI")
    private String tongyinci;

    @TableField(value = "NEWTONGYINCI")
    private String newtongyinci;

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }

    @TableField(value = "FENLEI")
    private String fenlei;

    public static final String COL_JMETASEARWORDID = "JMETASEARWORDID";

    public static final String COL_CRUSER = "CRUSER";

    public static final String COL_CRTIME = "CRTIME";

    public static final String COL_OPERUSER = "OPERUSER";

    public static final String COL_OPERTIME = "OPERTIME";

    public static final String COL_WORDNAME = "WORDNAME";

    public static final String COL_STATUS = "STATUS";

    public static final String COL_CATEGORY = "CATEGORY";

    public static final String COL_SOURCE = "SOURCE";

    public static final String COL_FILTE = "FILTE";

    public static final String COL_DOCSTATUS = "DOCSTATUS";

    public static final String COL_SEARCH_ORDER = "SEARCH_ORDER";

    public static final String COL_RECOMMEND = "RECOMMEND";

    public static final String COL_STOPWORD = "STOPWORD";

    public static final String COL_DOCTITLE = "DOCTITLE";

    public static final String COL_CXFL = "CXFL";

    public static final String COL_CTFL = "CTFL";

    public static final String COL_TONGYICI = "TONGYICI";

    public static final String COL_ENTONGYICI = "ENTONGYICI";

    public static final String COL_SUOXIE = "SUOXIE";

    public static final String COL_PINYIN = "PINYIN";

    public static final String COL_TONGYINCI = "TONGYINCI";

    public static final String COL_NEWTONGYINCI = "NEWTONGYINCI";

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public BigInteger getJmetasearwordid() {
        return jmetasearwordid;
    }

    public void setJmetasearwordid(BigInteger jmetasearwordid) {
        this.jmetasearwordid = jmetasearwordid;
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
     * 获取-1停用  0未生效 1 生效
     *
     * @return STATUS - -1停用  0未生效 1 生效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置-1停用  0未生效 1 生效
     *
     * @param status -1停用  0未生效 1 生效
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * @return FILTE
     */
    public Integer getFilte() {
        return filte;
    }

    /**
     * @param filte
     */
    public void setFilte(Integer filte) {
        this.filte = filte;
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
     * @return SEARCH_ORDER
     */
    public String getSearchOrder() {
        return searchOrder;
    }

    /**
     * @param searchOrder
     */
    public void setSearchOrder(String searchOrder) {
        this.searchOrder = searchOrder;
    }

    /**
     * @return RECOMMEND
     */
    public Integer getRecommend() {
        return recommend;
    }

    /**
     * @param recommend
     */
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    /**
     * @return STOPWORD
     */
    public String getStopword() {
        return stopword;
    }

    /**
     * @param stopword
     */
    public void setStopword(String stopword) {
        this.stopword = stopword;
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
     * @return CXFL
     */
    public String getCxfl() {
        return cxfl;
    }

    /**
     * @param cxfl
     */
    public void setCxfl(String cxfl) {
        this.cxfl = cxfl;
    }

    /**
     * @return CTFL
     */
    public String getCtfl() {
        return ctfl;
    }

    /**
     * @param ctfl
     */
    public void setCtfl(String ctfl) {
        this.ctfl = ctfl;
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
     * @return ENTONGYICI
     */
    public String getEntongyici() {
        return entongyici;
    }

    /**
     * @param entongyici
     */
    public void setEntongyici(String entongyici) {
        this.entongyici = entongyici;
    }

    /**
     * @return SUOXIE
     */
    public String getSuoxie() {
        return suoxie;
    }

    /**
     * @param suoxie
     */
    public void setSuoxie(String suoxie) {
        this.suoxie = suoxie;
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
     * @return NEWTONGYINCI
     */
    public String getNewtongyinci() {
        return newtongyinci;
    }

    /**
     * @param newtongyinci
     */
    public void setNewtongyinci(String newtongyinci) {
        this.newtongyinci = newtongyinci;
    }
}