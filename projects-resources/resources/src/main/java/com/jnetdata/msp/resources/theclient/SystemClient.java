package com.jnetdata.msp.resources.theclient;


import com.jnetdata.msp.config.config.model.ConfigModel;

public interface SystemClient {

    ConfigModel getById(Long id);

}
