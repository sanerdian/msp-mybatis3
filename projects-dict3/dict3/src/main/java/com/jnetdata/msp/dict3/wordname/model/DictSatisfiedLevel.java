package com.jnetdata.msp.dict3.wordname.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

@TableName(value = "dict_satisfied_level")
public class DictSatisfiedLevel {
    @TableId(value = "kid", type = IdType.AUTO)
    private Integer kid;

    @TableField(value = "level")
    private Integer level;

    @TableField(value = "descb")
    private String descb;

    @TableField(value = "crtime")
    private Date crtime;

    @TableField(value = "crusr")
    private String crusr;

    @TableField(value = "optime")
    private Date optime;

    @TableField(value = "opuser")
    private String opuser;

    public static final String COL_KID = "kid";

    public static final String COL_LEVEL = "level";

    public static final String COL_DESCB = "descb";

    public static final String COL_CRTIME = "crtime";

    public static final String COL_CRUSR = "crusr";

    public static final String COL_OPTIME = "optime";

    public static final String COL_OPUSER = "opuser";

    /**
     * @return kid
     */
    public Integer getKid() {
        return kid;
    }

    /**
     * @param kid
     */
    public void setKid(Integer kid) {
        this.kid = kid;
    }

    /**
     * @return level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return descb
     */
    public String getDescb() {
        return descb;
    }

    /**
     * @param descb
     */
    public void setDescb(String descb) {
        this.descb = descb;
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
     * @return crusr
     */
    public String getCrusr() {
        return crusr;
    }

    /**
     * @param crusr
     */
    public void setCrusr(String crusr) {
        this.crusr = crusr;
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