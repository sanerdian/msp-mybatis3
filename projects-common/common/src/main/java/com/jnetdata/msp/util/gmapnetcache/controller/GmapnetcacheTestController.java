package com.jnetdata.msp.util.gmapnetcache.controller;

import cn.hutool.core.io.FileUtil;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.util.gmapnetcache.model.Gmapnetcache;
import com.jnetdata.msp.util.gmapnetcache.service.GmapnetcacheService;
import com.jnetdata.msp.fenfa.service.FenfaService;
import com.jnetdata.msp.util.service.FieldinfoUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.web.JsonResult;

import java.sql.SQLException;
import java.util.*;

/**
 * <p>
 * Gmapnetcache 处理瓦片数据生成图片
 * </p>
 *
 * @author zyj
 * @since 2023-04-12
 */
@Controller
@RequestMapping("/cs1/gmapnetcache")
public class GmapnetcacheTestController extends BaseController<Long,Gmapnetcache> {

    final private GmapnetcacheService gmapnetcacheService;


    @Autowired
    private FieldinfoUtilService fieldinfoUtilService;

    @Autowired
    private FenfaService fenfaService;

    @Autowired
    public GmapnetcacheTestController(GmapnetcacheService gmapnetcacheService) {
        this.gmapnetcacheService = gmapnetcacheService;
    }


    /**
     * 执行新增操作
     * @return
     */
    @GetMapping("toPics")
    @ResponseBody
    public JsonResult toPics() throws SQLException {
        List<Gmapnetcache> list = getService().list();
        String path = "C:\\Users\\ASUS\\Desktop\\mappics\\";
        for (Gmapnetcache gmapnetcache : list) {
            FileUtil.writeBytes(gmapnetcache.getTile(),path + gmapnetcache.getZoom() + "\\" + gmapnetcache.getX() + "\\" + gmapnetcache.getY() + ".png");
        }
        return renderSuccess();
    }


    private GmapnetcacheService getService() {
        return this.gmapnetcacheService;
    }

}

