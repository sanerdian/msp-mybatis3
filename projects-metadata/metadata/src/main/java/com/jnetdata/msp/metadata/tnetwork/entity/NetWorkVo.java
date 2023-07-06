package com.jnetdata.msp.metadata.tnetwork.entity;

import lombok.Data;

import java.util.List;

@Data
public class NetWorkVo {
    private List<GroupEntiy> groupEntiy;
    private Paersonage paersonage;
    private List<UserEntity> userEntity;
    private String tablename;
    private String treename;
    private Long treeid;
    private Long xwid;

}
