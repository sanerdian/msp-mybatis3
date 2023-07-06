package com.jnetdata.msp.dict3.wordname.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.thenicesys.fastjson.serializer.ToStringSerializer;

import java.util.Date;

@TableName(value = "dict_dianpinglevel")
public class DictDianpinglevel {
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableId(value = "metaid", type = IdType.INPUT)
    private Long metaid;

    @TableField(value = "shuxinglevel")
    private Integer shuxinglevel;

    @TableField(value = "titile")
    private String titile;

    @TableField(value = "crtime")
    private Date crtime;

    @TableField(value = "optime")
    private Date optime;

    @TableField(value = "cruser")
    private String cruser;

    @TableField(value = "opuser")
    private String opuser;

    public static final String COL_METAID = "metaid";

    public static final String COL_SHUXINGLEVEL = "shuxinglevel";

    public static final String COL_TITILE = "titile";

    public static final String COL_CRTIME = "crtime";

    public static final String COL_OPTIME = "optime";

    public static final String COL_CRUSER = "cruser";

    public static final String COL_OPUSER = "opuser";

    /**
     * @return metaid
     */
    public Long getMetaid() {
        return metaid;
    }

    /**
     * @param metaid
     */
    public void setMetaid(Long metaid) {
        this.metaid = metaid;
    }

    /**
     * @return shuxinglevel
     */
    public Integer getShuxinglevel() {
        return shuxinglevel;
    }

    /**
     * @param shuxinglevel
     */
    public void setShuxinglevel(Integer shuxinglevel) {
        this.shuxinglevel = shuxinglevel;
    }

    /**
     * @return titile
     */
    public String getTitile() {
        return titile;
    }

    /**
     * @param titile
     */
    public void setTitile(String titile) {
        this.titile = titile;
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
     * @return optime
     */
    public Date getOptime() {
        return optime;
    }

    /**
     * @param optime
     */
    public void setOptime(Date optime) {
        this.optime = optime;
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
     * @return opuser
     */
    public String getOpuser() {
        return opuser;
    }

    /**
     * @param opuser
     */
    public void setOpuser(String opuser) {
        this.opuser = opuser;
    }
}