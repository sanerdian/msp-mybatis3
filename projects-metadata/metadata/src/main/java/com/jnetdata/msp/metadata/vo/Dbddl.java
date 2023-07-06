package com.jnetdata.msp.metadata.vo;

public enum Dbddl {

    MYSQL("mysql",false,false)
    ,ORACLE("oracle",true,true)
    ,DM("dm",true,true)
    ,KINGBASE("kingbase",true,true)
    ,SQLSERVER("SQLServerDialect",false,false)
    ,POSTGRESQL("postgresql",false,true);

    String dbtype;
    Boolean addSeq;
    Boolean addComment;

    Dbddl(String dbtype, Boolean addSeq, Boolean addComment) {
        this.dbtype = dbtype;
        this.addSeq = addSeq;
        this.addComment = addComment;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public Boolean getAddSeq() {
        return addSeq;
    }

    public void setAddSeq(Boolean addSeq) {
        this.addSeq = addSeq;
    }

    public Boolean getAddComment() {
        return addComment;
    }

    public void setAddComment(Boolean addComment) {
        this.addComment = addComment;
    }

    public static Dbddl getDbddl(String dbtype){
        for (Dbddl c : Dbddl.values()) {
            if (dbtype.equals(c.dbtype)) {
                return c;
            }
        }
        return null;
    }

}
