package com.jnetdata.msp.config.config.controller;

import com.jnetdata.msp.config.config.model.ConfigModel;
import com.jnetdata.msp.config.config.service.ConfigModelService;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.ChannelPathVO;
import com.jnetdata.msp.manage.publish.core.common.utils.AssertUtil;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishBasePathVO;
import com.jnetdata.msp.manage.publish.core.common.vo.PublishContextCache;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.template.model.Template;
import com.jnetdata.msp.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Admin on 2019/3/11.
 */

@Controller
@RequestMapping("/config")
@ApiModel(value = "ConfigController", description = "配置")
@Api(tags = "配置(ConfigController)")
public class ConfigController extends BaseController<Long, ConfigModel> {

    @Autowired
    private ConfigModelService service;
    @Resource
    private PublishContextCache publishContextCache;


    @ApiOperation(value = "添加", notes="添加配置信息")
    @PostMapping
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@RequestBody ConfigModel entity) {
        JsonResult<EntityId<Long>> entityIdJsonResult = doAdd(getService(), entity);
        cache();
        return entityIdJsonResult;
    }

    @ApiOperation(value = "删除", notes="删除指定id对应的配置信息")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@ApiParam("用户id") @PathVariable("id") Long id) {
        JsonResult<Void> voidJsonResult = doDeleteById(getService(), id);
        cache();
        return voidJsonResult;
    }

    @ApiOperation(value = "批量删除", notes="根据多个id删除配置信息")
    @GetMapping("/delByIds")
    @ResponseBody
    public JsonResult deleteBatchIds(@RequestParam("ids") String ids) {
        JsonResult<Void> voidJsonResult = doDeleteBatchIds(getService(), ids);
        cache();
        return voidJsonResult;
    }


    @ApiOperation(value = "修改", notes="修改指定id对应的配置信息(只需要填配置中的id)")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@ApiParam("用户id") @PathVariable("id") Long id,
                                 @RequestBody ConfigModel entity) {
        JsonResult<Void> voidJsonResult = doUpdateById(getService(), id, entity);
        cache();
        return voidJsonResult;
    }

    @ApiOperation(value = "根据id查询", notes="查看指定配置id对应的配置信息")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public JsonResult<ConfigModel> getById(@ApiParam("配置信息id") @PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }

    @ApiOperation(value = "根据实体属性查询", notes="根据vo查询")
    @PostMapping("/list")
    @ResponseBody
    public JsonResult<Pager<ConfigModel>> list(@RequestBody BaseVo<ConfigModel> vo) {
        return doList(getService(), vo.getPager(), vo.getEntity());
    }



    @ApiOperation(value = "批量上传", notes="通过excel批量上传数据")
    @PostMapping("/uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel( MultipartFile file)throws Exception{
        return renderSuccess();
    }


    @ApiOperation(value = "下载数据", notes="根据查询条件下载excel数据")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public JsonResult downloadExcel() {

        return renderSuccess();

    }

    @ApiOperation(value = "查询系统配置", notes="根据vo查询")
    @PostMapping("/getSys")
    @ResponseBody
    public JsonResult getSys(@RequestBody BaseVo<ConfigModel> vo) {
        return renderSuccess(service.getSystem(vo.getEntity()));
    }

    private ConfigModelService getService() {
        return service;
    }

    private void cache(){
        List<ConfigModel> list = getService().list(new PropertyWrapper<>(ConfigModel.class).eq("ctype",1));
        Map<String, ConfigModel> map = list.stream().collect(Collectors.toMap(ConfigModel::getMark
                , v -> v, (k1, k2) -> k1));

        ConfigModel dirUploadBase = map.get(ConfigModel.dir_front);
        AssertUtil.notNull(dirUploadBase, "根目录不能为空");

        File rootDirFile = new File(dirUploadBase.getValue());
        if (!rootDirFile.exists() || !rootDirFile.isDirectory()) {
            AssertUtil.notNull(dirUploadBase, "根目录不存在,请检查");
        }

        ConfigModel dirPic = map.get(ConfigModel.dir_pic);
        AssertUtil.notNull(dirPic, "附图目录不能为空");

        //ConfigModel webFront = map.get(ConfigModel.dir_front);

        ConfigModel dirPub = map.get(ConfigModel.dir_pub);
        AssertUtil.notNull(dirPub, "发布目录不能为空");

        ConfigModel dirPreview = map.get(ConfigModel.dir_preview);
        AssertUtil.notNull(dirPreview, "预览目录不能为空");

        ConfigModel dirFile = map.get(ConfigModel.dir_file);
        AssertUtil.notNull(dirFile, "附件目录不能为空");

        PublishBasePathVO publishBasePathVO = new PublishBasePathVO();

        String rootDir = dirUploadBase.getValue().replace("\\", "/");
        rootDir = rootDir.endsWith("/") ? rootDir : rootDir + "/";

        publishBasePathVO.setDirPub(dirPub.getValue());
        publishBasePathVO.setDirWebpic(dirPic.getValue());
        publishBasePathVO.setDirPreview(dirPreview.getValue());
        publishBasePathVO.setDirUpload(dirFile.getValue());
        publishBasePathVO.setUploadDirBase(rootDir);
        publishBasePathVO.setFinalWebpicPath(rootDir + publishBasePathVO.getDirWebpic());
        publishBasePathVO.setFinalPubDirPath(rootDir + publishBasePathVO.getDirPub());
        publishBasePathVO.setFinalPreviewDirPath(rootDir + publishBasePathVO.getDirPreview());
        publishBasePathVO.setFinalWebfilePath(rootDir + publishBasePathVO.getDirUpload());
        publishContextCache.setPublishBasePathVO(publishBasePathVO);
        publishContextCache.flashPath();
    }

}
