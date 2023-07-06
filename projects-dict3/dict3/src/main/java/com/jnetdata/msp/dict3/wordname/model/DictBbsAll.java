package com.jnetdata.msp.dict3.wordname.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFilter;
import org.springframework.format.annotation.DateTimeFormat;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@TableName(value = "dict_bbs_all")
public class DictBbsAll {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "jmetabbsid", type = IdType.AUTO)
    private Long jmetabbsid;

    @TableField(value = "cruser")
    private String cruser;

    @TableField(value = "crtime")
    private Date crtime;

    @TableField(value = "crint")
    private Integer crint;

    @TableField(value = "docchannelid")
    private Integer docchannelid;

    @TableField(value = "docstatus")
    private Integer docstatus;

    @TableField(value = "singletempkate")
    private Integer singletempkate;

    @TableField(value = "siteid")
    private Integer siteid;

    @TableField(value = "docvalid")
    private Date docvalid;

    @TableField(value = "docpubtime")
    private Date docpubtime;

    @TableField(value = "operuser")
    private String operuser;

    @TableField(value = "opertime")
    private Date opertime;

    @TableField(value = "doctitle")
    private String doctitle;

    @TableField(value = "docreltime")
    private Date docreltime;

    @TableField(value = "bbs_id")
    private Integer bbsId;

    @TableField(value = "bbs_bt")
    private String bbsBt;

    @TableField(value = "bbs_qa")
    private String bbsQa;

    @TableField(value = "bbs_jing")
    private String bbsJing;

    @TableField(value = "bbs_nr")
    private String bbsNr;

    @TableField(value = "bbs_sf")
    private String bbsSf;

    @TableField(value = "bbs_ds")
    private String bbsDs;

    @TableField(value = "bbs_ftsj")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bbsFtsj;

    @TableField(value = "bbs_hfs")
    private String bbsHfs;

    @TableField(value = "bbs_djs")
    private String bbsDjs;

    @TableField(value = "bbs_yhid")
    private String bbsYhid;

    @TableField(value = "bbs_zcrq")
    private String bbsZcrq;

    @TableField(value = "bbs_tablename")
    private String bbsTablename;

    @TableField(value = "bbs_cx")
    private String bbsCx;

    @TableField(value = "bbs_cxfl")
    private String bbsCxfl;

    @TableField(value = "keyword333")
    private String keyword333;

    @TableField(value = "keyword")
    private String keyword;

    @TableField(value = "adjectelation111")
    private String adjectelation111;

    @TableField(value = "t1")
    private String t1;

    @TableField(value = "t2")
    private String t2;

    @TableField(value = "t3")
    private String t3;

    @TableField(value = "t4")
    private String t4;

    @TableField(value = "prices")
    private String prices;

    @TableField(value = "brand")
    private String brand;

    @TableField(value = "satisfied")
    private Integer satisfied;

    @TableField(value = "neutral")
    private Integer neutral;

    @TableField(value = "unsatisfy")
    private Integer unsatisfy;

    @TableField(value = "ct_adj")
    private String ctAdj;

    @TableField(value = "cutpartime")
    private Date cutpartime;

    @TableField(value = "cutsentime")
    private Date cutsentime;

    @TableField(value = "adjectelation")
    private String adjectelation;

    @TableField(value = "keywordtime")
    private Date keywordtime;

    @TableField(value = "adj_starts")
    private Integer adjStarts;

    @TableField(value = "cut_status")
    private Integer cutStatus;

    @TableField(value = "ct_stopword")
    private String ctStopword;

    @TableField(value = "bbs_shsj")
    private Date bbsShsj;

    @TableField(value = "keytongyici")
    private String keytongyici;

    @TableField(value = "keyrelation")
    private String keyrelation;

    @TableField(value = "key_min")
    private Integer keyMin;

    @TableField(value = "key_max")
    private Integer keyMax;

    @TableField(value = "keynum")
    private Integer keynum;

    @TableField(value = "source")
    private String source;

    @TableField(value = "form_nr")
    private String formNr;

    @TableField(value = "classify")
    private String classify;

    @TableField(value = "nrlen")
    private Integer nrlen;

    @TableField(value = "keyword_ct")
    private String keywordCt;

    @TableField(value = "keyword_cx")
    private String keywordCx;

    @TableField(value = "iskoubei")
    private Integer iskoubei;

    @TableField(value = "car_id")
    private Integer carId;

    @TableField(value = "GENDER")
    private String gender;

    @TableField(value = "DOMICILE")
    private String domicile;

    @TableField(value = "YEARSOLD")
    private Integer yearsold;

    @TableField(value = "CHEKU")
    private String cheku;

    @TableField(value = "BBS_ERROR")
    private String bbsError;

    public static final String COL_JMETABBSID = "jmetabbsid";

    public static final String COL_CRUSER = "cruser";

    public static final String COL_CRTIME = "crtime";

    public static final String COL_CRINT = "crint";

    public static final String COL_DOCCHANNELID = "docchannelid";

    public static final String COL_DOCSTATUS = "docstatus";

    public static final String COL_SINGLETEMPKATE = "singletempkate";

    public static final String COL_SITEID = "siteid";

    public static final String COL_DOCVALID = "docvalid";

    public static final String COL_DOCPUBTIME = "docpubtime";

    public static final String COL_OPERUSER = "operuser";

    public static final String COL_OPERTIME = "opertime";

    public static final String COL_DOCTITLE = "doctitle";

    public static final String COL_DOCRELTIME = "docreltime";

    public static final String COL_BBS_ID = "bbs_id";

    public static final String COL_BBS_BT = "bbs_bt";

    public static final String COL_BBS_QA = "bbs_qa";

    public static final String COL_BBS_JING = "bbs_jing";

    public static final String COL_BBS_NR = "bbs_nr";

    public static final String COL_BBS_SF = "bbs_sf";

    public static final String COL_BBS_DS = "bbs_ds";

    public static final String COL_BBS_FTSJ = "bbs_ftsj";

    public static final String COL_BBS_HFS = "bbs_hfs";

    public static final String COL_BBS_DJS = "bbs_djs";

    public static final String COL_BBS_YHID = "bbs_yhid";

    public static final String COL_BBS_ZCRQ = "bbs_zcrq";

    public static final String COL_BBS_TABLENAME = "bbs_tablename";

    public static final String COL_BBS_CX = "bbs_cx";

    public static final String COL_BBS_CXFL = "bbs_cxfl";

    public static final String COL_KEYWORD333 = "keyword333";

    public static final String COL_KEYWORD = "keyword";

    public static final String COL_ADJECTELATION111 = "adjectelation111";

    public static final String COL_T1 = "t1";

    public static final String COL_T2 = "t2";

    public static final String COL_T3 = "t3";

    public static final String COL_T4 = "t4";

    public static final String COL_PRICES = "prices";

    public static final String COL_BRAND = "brand";

    public static final String COL_SATISFIED = "satisfied";

    public static final String COL_NEUTRAL = "neutral";

    public static final String COL_UNSATISFY = "unsatisfy";

    public static final String COL_CT_ADJ = "ct_adj";

    public static final String COL_CUTPARTIME = "cutpartime";

    public static final String COL_CUTSENTIME = "cutsentime";

    public static final String COL_ADJECTELATION = "adjectelation";

    public static final String COL_KEYWORDTIME = "keywordtime";

    public static final String COL_ADJ_STARTS = "adj_starts";

    public static final String COL_CUT_STATUS = "cut_status";

    public static final String COL_CT_STOPWORD = "ct_stopword";

    public static final String COL_BBS_SHSJ = "bbs_shsj";

    public static final String COL_KEYTONGYICI = "keytongyici";

    public static final String COL_KEYRELATION = "keyrelation";

    public static final String COL_KEY_MIN = "key_min";

    public static final String COL_KEY_MAX = "key_max";

    public static final String COL_KEYNUM = "keynum";

    public static final String COL_SOURCE = "source";

    public static final String COL_FORM_NR = "form_nr";

    public static final String COL_CLASSIFY = "classify";

    public static final String COL_NRLEN = "nrlen";

    public static final String COL_KEYWORD_CT = "keyword_ct";

    public static final String COL_KEYWORD_CX = "keyword_cx";

    public static final String COL_ISKOUBEI = "iskoubei";

    public static final String COL_CAR_ID = "car_id";

    public static final String COL_GENDER = "GENDER";

    public static final String COL_DOMICILE = "DOMICILE";

    public static final String COL_YEARSOLD = "YEARSOLD";

    public static final String COL_CHEKU = "CHEKU";

    public static final String COL_BBS_ERROR = "BBS_ERROR";

    /**
     * @return jmetabbsid
     */
    public Long getJmetabbsid() {
        return jmetabbsid;
    }

    /**
     * @param jmetabbsid
     */
    public void setJmetabbsid(Long jmetabbsid) {
        this.jmetabbsid = jmetabbsid;
    }

    /**
     * @return cruser
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
     * @return crtime
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
     * @return crint
     */
    public Integer getCrint() {
        return crint;
    }

    /**
     * @param crint
     */
    public void setCrint(Integer crint) {
        this.crint = crint;
    }

    /**
     * @return docchannelid
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
     * @return docstatus
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
     * @return singletempkate
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
     * @return siteid
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
     * @return docvalid
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
     * @return docpubtime
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
     * @return operuser
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
     * @return opertime
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
     * @return doctitle
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
     * @return docreltime
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
     * @return bbs_id
     */
    public Integer getBbsId() {
        return bbsId;
    }

    /**
     * @param bbsId
     */
    public void setBbsId(Integer bbsId) {
        this.bbsId = bbsId;
    }

    /**
     * @return bbs_bt
     */
    public String getBbsBt() {
        return bbsBt;
    }

    /**
     * @param bbsBt
     */
    public void setBbsBt(String bbsBt) {
        this.bbsBt = bbsBt;
    }

    /**
     * @return bbs_qa
     */
    public String getBbsQa() {
        return bbsQa;
    }

    /**
     * @param bbsQa
     */
    public void setBbsQa(String bbsQa) {
        this.bbsQa = bbsQa;
    }

    /**
     * @return bbs_jing
     */
    public String getBbsJing() {
        return bbsJing;
    }

    /**
     * @param bbsJing
     */
    public void setBbsJing(String bbsJing) {
        this.bbsJing = bbsJing;
    }

    /**
     * @return bbs_nr
     */
    public String getBbsNr() {
        return bbsNr;
    }

    /**
     * @param bbsNr
     */
    public void setBbsNr(String bbsNr) {
        this.bbsNr = bbsNr;
    }

    /**
     * @return bbs_sf
     */
    public String getBbsSf() {
        return bbsSf;
    }

    /**
     * @param bbsSf
     */
    public void setBbsSf(String bbsSf) {
        this.bbsSf = bbsSf;
    }

    /**
     * @return bbs_ds
     */
    public String getBbsDs() {
        return bbsDs;
    }

    /**
     * @param bbsDs
     */
    public void setBbsDs(String bbsDs) {
        this.bbsDs = bbsDs;
    }

    /**
     * @return bbs_ftsj
     */
    public Date getBbsFtsj() {
        return bbsFtsj;
    }

    /**
     * @param bbsFtsj
     */
    public void setBbsFtsj(Date bbsFtsj) {
        this.bbsFtsj = bbsFtsj;
    }

    /**
     * @return bbs_hfs
     */
    public String getBbsHfs() {
        return bbsHfs;
    }

    /**
     * @param bbsHfs
     */
    public void setBbsHfs(String bbsHfs) {
        this.bbsHfs = bbsHfs;
    }

    /**
     * @return bbs_djs
     */
    public String getBbsDjs() {
        return bbsDjs;
    }

    /**
     * @param bbsDjs
     */
    public void setBbsDjs(String bbsDjs) {
        this.bbsDjs = bbsDjs;
    }

    /**
     * @return bbs_yhid
     */
    public String getBbsYhid() {
        return bbsYhid;
    }

    /**
     * @param bbsYhid
     */
    public void setBbsYhid(String bbsYhid) {
        this.bbsYhid = bbsYhid;
    }

    /**
     * @return bbs_zcrq
     */
    public String getBbsZcrq() {
        return bbsZcrq;
    }

    /**
     * @param bbsZcrq
     */
    public void setBbsZcrq(String bbsZcrq) {
        this.bbsZcrq = bbsZcrq;
    }

    /**
     * @return bbs_tablename
     */
    public String getBbsTablename() {
        return bbsTablename;
    }

    /**
     * @param bbsTablename
     */
    public void setBbsTablename(String bbsTablename) {
        this.bbsTablename = bbsTablename;
    }

    /**
     * @return bbs_cx
     */
    public String getBbsCx() {
        return bbsCx;
    }

    /**
     * @param bbsCx
     */
    public void setBbsCx(String bbsCx) {
        this.bbsCx = bbsCx;
    }

    /**
     * @return bbs_cxfl
     */
    public String getBbsCxfl() {
        return bbsCxfl;
    }

    /**
     * @param bbsCxfl
     */
    public void setBbsCxfl(String bbsCxfl) {
        this.bbsCxfl = bbsCxfl;
    }

    /**
     * @return keyword333
     */
    public String getKeyword333() {
        return keyword333;
    }

    /**
     * @param keyword333
     */
    public void setKeyword333(String keyword333) {
        this.keyword333 = keyword333;
    }

    /**
     * @return keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return adjectelation111
     */
    public String getAdjectelation111() {
        return adjectelation111;
    }

    /**
     * @param adjectelation111
     */
    public void setAdjectelation111(String adjectelation111) {
        this.adjectelation111 = adjectelation111;
    }

    /**
     * @return t1
     */
    public String getT1() {
        return t1;
    }

    /**
     * @param t1
     */
    public void setT1(String t1) {
        this.t1 = t1;
    }

    /**
     * @return t2
     */
    public String getT2() {
        return t2;
    }

    /**
     * @param t2
     */
    public void setT2(String t2) {
        this.t2 = t2;
    }

    /**
     * @return t3
     */
    public String getT3() {
        return t3;
    }

    /**
     * @param t3
     */
    public void setT3(String t3) {
        this.t3 = t3;
    }

    /**
     * @return t4
     */
    public String getT4() {
        return t4;
    }

    /**
     * @param t4
     */
    public void setT4(String t4) {
        this.t4 = t4;
    }

    /**
     * @return prices
     */
    public String getPrices() {
        return prices;
    }

    /**
     * @param prices
     */
    public void setPrices(String prices) {
        this.prices = prices;
    }

    /**
     * @return brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return satisfied
     */
    public Integer getSatisfied() {
        return satisfied;
    }

    /**
     * @param satisfied
     */
    public void setSatisfied(Integer satisfied) {
        this.satisfied = satisfied;
    }

    /**
     * @return neutral
     */
    public Integer getNeutral() {
        return neutral;
    }

    /**
     * @param neutral
     */
    public void setNeutral(Integer neutral) {
        this.neutral = neutral;
    }

    /**
     * @return unsatisfy
     */
    public Integer getUnsatisfy() {
        return unsatisfy;
    }

    /**
     * @param unsatisfy
     */
    public void setUnsatisfy(Integer unsatisfy) {
        this.unsatisfy = unsatisfy;
    }

    /**
     * @return ct_adj
     */
    public String getCtAdj() {
        return ctAdj;
    }

    /**
     * @param ctAdj
     */
    public void setCtAdj(String ctAdj) {
        this.ctAdj = ctAdj;
    }

    /**
     * @return cutpartime
     */
    public Date getCutpartime() {
        return cutpartime;
    }

    /**
     * @param cutpartime
     */
    public void setCutpartime(Date cutpartime) {
        this.cutpartime = cutpartime;
    }

    /**
     * @return cutsentime
     */
    public Date getCutsentime() {
        return cutsentime;
    }

    /**
     * @param cutsentime
     */
    public void setCutsentime(Date cutsentime) {
        this.cutsentime = cutsentime;
    }

    /**
     * @return adjectelation
     */
    public String getAdjectelation() {
        return adjectelation;
    }

    /**
     * @param adjectelation
     */
    public void setAdjectelation(String adjectelation) {
        this.adjectelation = adjectelation;
    }

    /**
     * @return keywordtime
     */
    public Date getKeywordtime() {
        return keywordtime;
    }

    /**
     * @param keywordtime
     */
    public void setKeywordtime(Date keywordtime) {
        this.keywordtime = keywordtime;
    }

    /**
     * @return adj_starts
     */
    public Integer getAdjStarts() {
        return adjStarts;
    }

    /**
     * @param adjStarts
     */
    public void setAdjStarts(Integer adjStarts) {
        this.adjStarts = adjStarts;
    }

    /**
     * @return cut_status
     */
    public Integer getCutStatus() {
        return cutStatus;
    }

    /**
     * @param cutStatus
     */
    public void setCutStatus(Integer cutStatus) {
        this.cutStatus = cutStatus;
    }

    /**
     * @return ct_stopword
     */
    public String getCtStopword() {
        return ctStopword;
    }

    /**
     * @param ctStopword
     */
    public void setCtStopword(String ctStopword) {
        this.ctStopword = ctStopword;
    }

    /**
     * @return bbs_shsj
     */
    public Date getBbsShsj() {
        return bbsShsj;
    }

    /**
     * @param bbsShsj
     */
    public void setBbsShsj(Date bbsShsj) {
        this.bbsShsj = bbsShsj;
    }

    /**
     * @return keytongyici
     */
    public String getKeytongyici() {
        return keytongyici;
    }

    /**
     * @param keytongyici
     */
    public void setKeytongyici(String keytongyici) {
        this.keytongyici = keytongyici;
    }

    /**
     * @return keyrelation
     */
    public String getKeyrelation() {
        return keyrelation;
    }

    /**
     * @param keyrelation
     */
    public void setKeyrelation(String keyrelation) {
        this.keyrelation = keyrelation;
    }

    /**
     * @return key_min
     */
    public Integer getKeyMin() {
        return keyMin;
    }

    /**
     * @param keyMin
     */
    public void setKeyMin(Integer keyMin) {
        this.keyMin = keyMin;
    }

    /**
     * @return key_max
     */
    public Integer getKeyMax() {
        return keyMax;
    }

    /**
     * @param keyMax
     */
    public void setKeyMax(Integer keyMax) {
        this.keyMax = keyMax;
    }

    /**
     * @return keynum
     */
    public Integer getKeynum() {
        return keynum;
    }

    /**
     * @param keynum
     */
    public void setKeynum(Integer keynum) {
        this.keynum = keynum;
    }

    /**
     * @return source
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
     * @return form_nr
     */
    public String getFormNr() {
        return formNr;
    }

    /**
     * @param formNr
     */
    public void setFormNr(String formNr) {
        this.formNr = formNr;
    }

    /**
     * @return classify
     */
    public String getClassify() {
        return classify;
    }

    /**
     * @param classify
     */
    public void setClassify(String classify) {
        this.classify = classify;
    }

    /**
     * @return nrlen
     */
    public Integer getNrlen() {
        return nrlen;
    }

    /**
     * @param nrlen
     */
    public void setNrlen(Integer nrlen) {
        this.nrlen = nrlen;
    }

    /**
     * @return keyword_ct
     */
    public String getKeywordCt() {
        return keywordCt;
    }

    /**
     * @param keywordCt
     */
    public void setKeywordCt(String keywordCt) {
        this.keywordCt = keywordCt;
    }

    /**
     * @return keyword_cx
     */
    public String getKeywordCx() {
        return keywordCx;
    }

    /**
     * @param keywordCx
     */
    public void setKeywordCx(String keywordCx) {
        this.keywordCx = keywordCx;
    }

    /**
     * @return iskoubei
     */
    public Integer getIskoubei() {
        return iskoubei;
    }

    /**
     * @param iskoubei
     */
    public void setIskoubei(Integer iskoubei) {
        this.iskoubei = iskoubei;
    }

    /**
     * @return car_id
     */
    public Integer getCarId() {
        return carId;
    }

    /**
     * @param carId
     */
    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    /**
     * @return GENDER
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return DOMICILE
     */
    public String getDomicile() {
        return domicile;
    }

    /**
     * @param domicile
     */
    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    /**
     * @return YEARSOLD
     */
    public Integer getYearsold() {
        return yearsold;
    }

    /**
     * @param yearsold
     */
    public void setYearsold(Integer yearsold) {
        this.yearsold = yearsold;
    }

    /**
     * @return CHEKU
     */
    public String getCheku() {
        return cheku;
    }

    /**
     * @param cheku
     */
    public void setCheku(String cheku) {
        this.cheku = cheku;
    }

    /**
     * @return BBS_ERROR
     */
    public String getBbsError() {
        return bbsError;
    }

    /**
     * @param bbsError
     */
    public void setBbsError(String bbsError) {
        this.bbsError = bbsError;
    }
}