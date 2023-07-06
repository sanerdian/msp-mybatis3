package com.jnetdata.msp.cs1.esunstructured.model;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

@Data
public class Esunstructured {
    public Esunstructured(){}

    public Esunstructured(Map<String,Object> map){
        this.id = (String)map.get("id");
        this.filetype = (String)map.get("filetype");
        this.pagecount = (String)map.get("pageCount");
        this.filelink = (String)map.get("filelink");
        this.year = (String)map.get("year");
        this.author = (String)map.get("author");
        this.sourcesystem = (String)map.get("sourceSystem");
        this.description = (String)map.get("description");
        this.filesize = (String)map.get("filesize");
        this.label = (String)map.get("label");
        this.title = (String)map.get("title");
        this.type = (String)map.get("type");
        this.content = (String)map.get("content");
        this.fastdevid = (String)map.get("fastdevid");
        this.department = (String)map.get("department");
        this.updatetime = (String)map.get("updatetime");
        this.updateuser = (String)map.get("updateuser");
        this._score = (String)map.get("_score");
    }

    private static final long serialVersionUID=1L;


    private String id;
    private String _score;


    private String filetype;

    private String pagecount;

    private String filelink;

    private String year;

    private String author;

    private String sourcesystem;

    private String description;

    private String filesize;

    private String label;

    private String title;

    private String type;

    private String content;

    private String fastdevid;

    private String department;

    private String updatetime;

    private String updateuser;


    @ApiModelProperty("检索多个字段")
    private String commonKey;
    private int searchType;
    @ApiModelProperty("要检索的字段,不传为检索所有字段")
    private String[] searchFields;
    private List<String> highlightInfos;
    @ApiModelProperty("高亮字段")
    private Map<String,Object> hightlightFields;

    @ApiModelProperty(hidden = true)
    public Map<String,Object> getEsMap(){
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(this.filetype))  map.put("filetype", this.filetype);
        if(StringUtils.isNotEmpty(this.pagecount))  map.put("pageCount", this.pagecount);
        if(StringUtils.isNotEmpty(this.filelink))  map.put("filelink", this.filelink);
        if(StringUtils.isNotEmpty(this.year))  map.put("year", this.year);
        if(StringUtils.isNotEmpty(this.author))  map.put("author", this.author);
        if(StringUtils.isNotEmpty(this.sourcesystem))  map.put("sourceSystem", this.sourcesystem);
        if(StringUtils.isNotEmpty(this.description))  map.put("description", this.description);
        if(StringUtils.isNotEmpty(this.filesize))  map.put("filesize", this.filesize);
        if(StringUtils.isNotEmpty(this.label))  map.put("label", this.label);
        if(StringUtils.isNotEmpty(this.title))  map.put("title", this.title);
        if(StringUtils.isNotEmpty(this.type))  map.put("type", this.type);
        if(StringUtils.isNotEmpty(this.content))  map.put("content", this.content);
        if(StringUtils.isNotEmpty(this.fastdevid))  map.put("fastdevid", this.fastdevid);
        if(StringUtils.isNotEmpty(this.department))  map.put("department", this.department);
        if(StringUtils.isNotEmpty(this.updatetime))  map.put("updatetime", this.updatetime);
        if(StringUtils.isNotEmpty(this.updateuser))  map.put("updateuser", this.updateuser);
        return map;
    }
}
