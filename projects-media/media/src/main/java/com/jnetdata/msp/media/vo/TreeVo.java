package com.jnetdata.msp.media.vo;

import lombok.Data;

import java.util.List;

@Data
public class TreeVo {

    private String id;

    private String pId;

    private String name;

    private List<String > dwlist;
}
