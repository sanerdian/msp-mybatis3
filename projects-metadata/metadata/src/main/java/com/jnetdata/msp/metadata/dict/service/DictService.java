package com.jnetdata.msp.metadata.dict.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.metadata.dict.model.Dict;

import java.util.List;
import java.util.Map;

public interface DictService extends BaseService<Dict> {

    List<Dict> list(String type);

    Map<Integer, String> map(String type);

    Dict get(String type, int no);
}