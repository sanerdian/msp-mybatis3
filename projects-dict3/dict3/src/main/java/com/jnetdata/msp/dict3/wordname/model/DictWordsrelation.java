package com.jnetdata.msp.dict3.wordname.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
@Data
@TableName(value = "dict_wordsrelation")
public class DictWordsrelation {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "JMETAWORDRELATIONID", type = IdType.INPUT)
//    @TableField(value = "JMETAWORDRELATIONID")
    private BigInteger jmetawordrelationid;

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

    @TableField(value = "TONGYICI")
    private String tongyici;

    @TableField(value = "HOMOIONYM")
    private String homoionym;

    @TableField(value = "RELATEDWORD")
    private String relatedword;

    @TableField(value = "UPPERWORD")
    private String upperword;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField(value = "WORDID")
    private Long wordid;

    @TableField(value = "fenlei")
    private String fenlei;

    @TableField(value = "INFERIORWORDS")
    private String inferiorwords;

    @TableField(value = "PINYINWORD")
    private String pinyinword;

    @TableField(value = "ENGLISHWORD")
    private String englishword;

    @TableField(value = "TYPE")
    private String type;

    @TableField(value = "MARK")
    private Long mark;

    @TableField(value = "SPELL")
    private String spell;

    @TableField(value = "DOCSTATUS")
    private String docstatus;

    @TableField(value = "DOCTITLE")
    private String doctitle;

    @TableField(value = "CXFL")
    private String cxfl;

    @TableField(value = "CTFL")
    private String ctfl;

    @TableField(value = "CATEGORY")
    private String category;

    @TableField(value = "CT_ADJ")
    private String ctAdj;

    @TableField(value = "RULE")
    private String rule;

    @TableField(value = "REMARKS")
    private String remarks;

    @TableField(value = "SHUXING")
    private String shuxing;

    @TableField(value = "TONGYINCI")
    private String tongyinci;

    @TableField(value = "RELPINYIN")
    private String relpinyin;

    @TableField(value = "RELTONGYINCI")
    private String reltongyinci;

    @TableField(value = "SXTONGYINCI")
    private String sxtongyinci;

    @TableField(value = "SXTONGYICI")
    private String sxtongyici;

    @TableField(value = "SXPINYIN")
    private String sxpinyin;

    @TableField(value = "t3")
    private String t3;

    @TableField(value = "t4")
    private String t4;

    @TableField(value = "ZBFENLEI")
    private Integer zbfenlei;

    @TableField(value = "T1")
    private String t1;

    @TableField(value = "T2")
    private String t2;

    @TableField(value = "FLAG")
    private Integer flag;

    @TableField(value = "TINGYONGCI")
    private String tingyongci;

    @TableField(value = "CHANGJINGCI")
    private String changjingci;

    @TableField(value = "ZTTONGYICI")
    private String zttongyici;

    @TableField(value = "pinyin")
    private String pinyin;

    @TableField(value = "shuxingtongyici")
    private String shuxingtongyici;

    @TableField(value = "shuxingpinyin")
    private String shuxingpinyin;

    @TableField(value = "zhutistopwordname")
    private String zhutistopwordname;

    @TableField(value = "relationword")
    private String relationword;

    @TableField(value = "relatedtongyici")
    private String relatedtongyici;

    @TableField(value = "relatedpinyin")
    private String relatedpinyin;

    @TableField(value = "relatedforum")
    private String relatedforum;

    @TableField(value = "status")
    private String status;
//    public static final String COL_JMETAWORDRELATIONID = "JMETAWORDRELATIONID";
//
//    public static final String COL_CRUSER = "CRUSER";
//
//    public static final String COL_CRTIME = "CRTIME";
//
//    public static final String COL_OPERUSER = "OPERUSER";
//
//    public static final String COL_OPERTIME = "OPERTIME";
//
//    public static final String COL_WORDNAME = "WORDNAME";
//
//    public static final String COL_TONGYICI = "TONGYICI";
//
//    public static final String COL_HOMOIONYM = "HOMOIONYM";
//
//    public static final String COL_RELATEDWORD = "RELATEDWORD";
//
//    public static final String COL_UPPERWORD = "UPPERWORD";
//
//    public static final String COL_INFERIORWORDS = "INFERIORWORDS";
//
//    public static final String COL_PINYINWORD = "PINYINWORD";
//
//    public static final String COL_ENGLISHWORD = "ENGLISHWORD";
//
//    public static final String COL_TYPE = "TYPE";
//
//    public static final String COL_MARK = "MARK";
//
//    public static final String COL_SPELL = "SPELL";
//
//    public static final String COL_DOCSTATUS = "DOCSTATUS";
//
//    public static final String COL_DOCTITLE = "DOCTITLE";
//
//    public static final String COL_CXFL = "CXFL";
//
//    public static final String COL_CTFL = "CTFL";
//
//    public static final String COL_CATEGORY = "CATEGORY";
//
//    public static final String COL_CT_ADJ = "CT_ADJ";
//
//    public static final String COL_RULE = "RULE";
//
//    public static final String COL_REMARKS = "REMARKS";
//
//    public static final String COL_SHUXING = "SHUXING";
//
//    public static final String COL_TONGYINCI = "TONGYINCI";
//
//    public static final String COL_RELPINYIN = "RELPINYIN";
//
//    public static final String COL_RELTONGYINCI = "RELTONGYINCI";
//
//    public static final String COL_SXTONGYINCI = "SXTONGYINCI";
//
//    public static final String COL_SXTONGYICI = "SXTONGYICI";
//
//    public static final String COL_SXPINYIN = "SXPINYIN";
//
//    public static final String COL_CTFL_SANJI = "CTFL_SANJI";
//
//    public static final String COL_ZBFENLEI = "ZBFENLEI";
//
//    public static final String COL_T1 = "T1";
//
//    public static final String COL_T2 = "T2";
//
//    public static final String COL_FLAG = "FLAG";
//
//    public static final String COL_TINGYONGCI = "TINGYONGCI";
//
//    public static final String COL_CHANGJINGCI = "CHANGJINGCI";
//
//    public static final String COL_ZTTONGYICI = "ZTTONGYICI";

//    /**
//     * @return JMETAWORDRELATIONID
//     */
//    public Long getJmetawordrelationid() {
//        return jmetawordrelationid;
//    }
//
//    /**
//     * @param jmetawordrelationid
//     */
//    public void setJmetawordrelationid(Long jmetawordrelationid) {
//        this.jmetawordrelationid = jmetawordrelationid;
//    }
//
//    /**
//     * @return CRUSER
//     */
//    public String getCruser() {
//        return cruser;
//    }
//
//    /**
//     * @param cruser
//     */
//    public void setCruser(String cruser) {
//        this.cruser = cruser;
//    }
//
//    /**
//     * @return CRTIME
//     */
//    public Date getCrtime() {
//        return crtime;
//    }
//
//    /**
//     * @param crtime
//     */
//    public void setCrtime(Date crtime) {
//        this.crtime = crtime;
//    }
//
//    public String getFenlei() {
//        return fenlei;
//    }
//
//    public void setFenlei(String fenlei) {
//        this.fenlei = fenlei;
//    }
//
//    /**
//     * @return OPERUSER
//     */
//    public String getOperuser() {
//        return operuser;
//    }
//
//    /**
//     * @param operuser
//     */
//    public void setOperuser(String operuser) {
//        this.operuser = operuser;
//    }
//
//    /**
//     * @return OPERTIME
//     */
//    public Date getOpertime() {
//        return opertime;
//    }
//
//    /**
//     * @param opertime
//     */
//    public void setOpertime(Date opertime) {
//        this.opertime = opertime;
//    }
//
//    /**
//     * @return WORDNAME
//     */
//    public String getWordname() {
//        return wordname;
//    }
//
//    /**
//     * @param wordname
//     */
//    public void setWordname(String wordname) {
//        this.wordname = wordname;
//    }
//
//    /**
//     * @return TONGYICI
//     */
//    public String getTongyici() {
//        return tongyici;
//    }
//
//    /**
//     * @param tongyici
//     */
//    public void setTongyici(String tongyici) {
//        this.tongyici = tongyici;
//    }
//
//    /**
//     * @return HOMOIONYM
//     */
//    public String getHomoionym() {
//        return homoionym;
//    }
//
//    /**
//     * @param homoionym
//     */
//    public void setHomoionym(String homoionym) {
//        this.homoionym = homoionym;
//    }
//
//    /**
//     * @return RELATEDWORD
//     */
//    public String getRelatedword() {
//        return relatedword;
//    }
//
//    /**
//     * @param relatedword
//     */
//    public void setRelatedword(String relatedword) {
//        this.relatedword = relatedword;
//    }
//
//    /**
//     * @return UPPERWORD
//     */
//    public String getUpperword() {
//        return upperword;
//    }
//
//    /**
//     * @param upperword
//     */
//    public void setUpperword(String upperword) {
//        this.upperword = upperword;
//    }
//
//    /**
//     * @return INFERIORWORDS
//     */
//    public String getInferiorwords() {
//        return inferiorwords;
//    }
//
//    /**
//     * @param inferiorwords
//     */
//    public void setInferiorwords(String inferiorwords) {
//        this.inferiorwords = inferiorwords;
//    }
//
//    /**
//     * @return PINYINWORD
//     */
//    public String getPinyinword() {
//        return pinyinword;
//    }
//
//    /**
//     * @param pinyinword
//     */
//    public void setPinyinword(String pinyinword) {
//        this.pinyinword = pinyinword;
//    }
//
//    /**
//     * @return ENGLISHWORD
//     */
//    public String getEnglishword() {
//        return englishword;
//    }
//
//    /**
//     * @param englishword
//     */
//    public void setEnglishword(String englishword) {
//        this.englishword = englishword;
//    }
//
//    /**
//     * @return TYPE
//     */
//    public String getType() {
//        return type;
//    }
//
//    /**
//     * @param type
//     */
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    /**
//     * @return MARK
//     */
//    public Long getMark() {
//        return mark;
//    }
//
//    /**
//     * @param mark
//     */
//    public void setMark(Long mark) {
//        this.mark = mark;
//    }
//
//    /**
//     * @return SPELL
//     */
//    public String getSpell() {
//        return spell;
//    }
//
//    /**
//     * @param spell
//     */
//    public void setSpell(String spell) {
//        this.spell = spell;
//    }
//
//    /**
//     * @return DOCSTATUS
//     */
//    public String getDocstatus() {
//        return docstatus;
//    }
//
//    /**
//     * @param docstatus
//     */
//    public void setDocstatus(String docstatus) {
//        this.docstatus = docstatus;
//    }
//
//    /**
//     * @return DOCTITLE
//     */
//    public String getDoctitle() {
//        return doctitle;
//    }
//
//    /**
//     * @param doctitle
//     */
//    public void setDoctitle(String doctitle) {
//        this.doctitle = doctitle;
//    }
//
//    /**
//     * @return CXFL
//     */
//    public String getCxfl() {
//        return cxfl;
//    }
//
//    /**
//     * @param cxfl
//     */
//    public void setCxfl(String cxfl) {
//        this.cxfl = cxfl;
//    }
//
//    /**
//     * @return CTFL
//     */
//    public String getCtfl() {
//        return ctfl;
//    }
//
//    /**
//     * @param ctfl
//     */
//    public void setCtfl(String ctfl) {
//        this.ctfl = ctfl;
//    }
//
//    /**
//     * @return CATEGORY
//     */
//    public String getCategory() {
//        return category;
//    }
//
//    /**
//     * @param category
//     */
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    /**
//     * @return CT_ADJ
//     */
//    public String getCtAdj() {
//        return ctAdj;
//    }
//
//    /**
//     * @param ctAdj
//     */
//    public void setCtAdj(String ctAdj) {
//        this.ctAdj = ctAdj;
//    }
//
//    /**
//     * @return RULE
//     */
//    public String getRule() {
//        return rule;
//    }
//
//    /**
//     * @param rule
//     */
//    public void setRule(String rule) {
//        this.rule = rule;
//    }
//
//    /**
//     * @return REMARKS
//     */
//    public String getRemarks() {
//        return remarks;
//    }
//
//    /**
//     * @param remarks
//     */
//    public void setRemarks(String remarks) {
//        this.remarks = remarks;
//    }
//
//    /**
//     * @return SHUXING
//     */
//    public String getShuxing() {
//        return shuxing;
//    }
//
//    /**
//     * @param shuxing
//     */
//    public void setShuxing(String shuxing) {
//        this.shuxing = shuxing;
//    }
//
//    /**
//     * @return TONGYINCI
//     */
//    public String getTongyinci() {
//        return tongyinci;
//    }
//
//    /**
//     * @param tongyinci
//     */
//    public void setTongyinci(String tongyinci) {
//        this.tongyinci = tongyinci;
//    }
//
//    /**
//     * @return RELPINYIN
//     */
//    public String getRelpinyin() {
//        return relpinyin;
//    }
//
//    /**
//     * @param relpinyin
//     */
//    public void setRelpinyin(String relpinyin) {
//        this.relpinyin = relpinyin;
//    }
//
//    /**
//     * @return RELTONGYINCI
//     */
//    public String getReltongyinci() {
//        return reltongyinci;
//    }
//
//    /**
//     * @param reltongyinci
//     */
//    public void setReltongyinci(String reltongyinci) {
//        this.reltongyinci = reltongyinci;
//    }
//
//    /**
//     * @return SXTONGYINCI
//     */
//    public String getSxtongyinci() {
//        return sxtongyinci;
//    }
//
//    /**
//     * @param sxtongyinci
//     */
//    public void setSxtongyinci(String sxtongyinci) {
//        this.sxtongyinci = sxtongyinci;
//    }
//
//    /**
//     * @return SXTONGYICI
//     */
//    public String getSxtongyici() {
//        return sxtongyici;
//    }
//
//    /**
//     * @param sxtongyici
//     */
//    public void setSxtongyici(String sxtongyici) {
//        this.sxtongyici = sxtongyici;
//    }
//
//    /**
//     * @return SXPINYIN
//     */
//    public String getSxpinyin() {
//        return sxpinyin;
//    }
//
//    /**
//     * @param sxpinyin
//     */
//    public void setSxpinyin(String sxpinyin) {
//        this.sxpinyin = sxpinyin;
//    }
//
//    /**
//     * @return CTFL_SANJI
//     */
//    public String getCtflSanji() {
//        return ctflSanji;
//    }
//
//    /**
//     * @param ctflSanji
//     */
//    public void setCtflSanji(String ctflSanji) {
//        this.ctflSanji = ctflSanji;
//    }
//
//    /**
//     * @return ZBFENLEI
//     */
//    public Integer getZbfenlei() {
//        return zbfenlei;
//    }
//
//    /**
//     * @param zbfenlei
//     */
//    public void setZbfenlei(Integer zbfenlei) {
//        this.zbfenlei = zbfenlei;
//    }
//
//    /**
//     * @return T1
//     */
//    public String getT1() {
//        return t1;
//    }
//
//    /**
//     * @param t1
//     */
//    public void setT1(String t1) {
//        this.t1 = t1;
//    }
//
//    /**
//     * @return T2
//     */
//    public String getT2() {
//        return t2;
//    }
//
//    /**
//     * @param t2
//     */
//    public void setT2(String t2) {
//        this.t2 = t2;
//    }
//
//    /**
//     * @return FLAG
//     */
//    public Integer getFlag() {
//        return flag;
//    }
//
//    /**
//     * @param flag
//     */
//    public void setFlag(Integer flag) {
//        this.flag = flag;
//    }
//
//    /**
//     * @return TINGYONGCI
//     */
//    public String getTingyongci() {
//        return tingyongci;
//    }
//
//    /**
//     * @param tingyongci
//     */
//    public void setTingyongci(String tingyongci) {
//        this.tingyongci = tingyongci;
//    }
//
//    /**
//     * @return CHANGJINGCI
//     */
//    public String getChangjingci() {
//        return changjingci;
//    }
//
//    /**
//     * @param changjingci
//     */
//    public void setChangjingci(String changjingci) {
//        this.changjingci = changjingci;
//    }
//
//    public Long getWordid() {
//        return wordid;
//    }
//
//    public void setWordid(Long wordid) {
//        this.wordid = wordid;
//    }
//
//    /**
//     * @return ZTTONGYICI
//     */
//    public String getZttongyici() {
//        return zttongyici;
//    }
//
//    /**
//     * @param zttongyici
//     */
//    public void setZttongyici(String zttongyici) {
//        this.zttongyici = zttongyici;
//    }
}