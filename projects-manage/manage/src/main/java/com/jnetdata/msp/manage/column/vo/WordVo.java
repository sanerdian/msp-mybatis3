package com.jnetdata.msp.manage.column.vo;

import com.jnetdata.msp.manage.column.model.Programa;
import lombok.Data;
import org.thenicesys.web.vo.PageVo1;

import java.util.List;

@Data
public class WordVo {
    String str;
    List<Long> ids;
    List<Long> siteIds;
    Integer tplType;
}
