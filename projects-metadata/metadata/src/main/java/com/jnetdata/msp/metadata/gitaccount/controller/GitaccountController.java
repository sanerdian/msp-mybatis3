package com.jnetdata.msp.metadata.gitaccount.controller;


import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.metadata.gitaccount.model.Gitaccount;
import com.jnetdata.msp.metadata.gitaccount.service.GitaccountService;
import com.jnetdata.msp.metadata.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyj
 * @since 2019-09-23
 */
@Controller
@RequestMapping("/metadata/gitaccount")
@ApiModel(value = "git账号管理(GitaccountController)")
public class GitaccountController extends BaseController<Long, Gitaccount> {

    @Autowired
    private GitaccountService gitaccountService;


    /**
     * 执行新增操作
     * @param entity
     * @return
     */
    @ApiOperation(value = "添加实体对象")
    @PostMapping(value = "/add")
    @ResponseBody
    public JsonResult<EntityId<Long>> add(@Validated @RequestBody Gitaccount entity) {
        return doAdd(getService(), entity);
    }

    /**
     * 执行删除操作
     * @param id
     * @return
     */
    @ApiOperation(value = "删除",notes = "根据指定id删除")
    @DeleteMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> deleteById(@PathVariable("id") Long id) {
        return doDeleteById(getService(), id);
    }

    /**
     * 执行批量删除操作
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除",notes = "根据指定的多个id批量删除")
    @DeleteMapping("/{ids}/batch")
    @ResponseBody
    public JsonResult<Void> deleteBatchIds(@PathVariable("ids") @ApiParam("多个id用逗号隔开")String ids) {
        return doDeleteBatchIds(getService(), Arrays.asList(ids));
    }

    /**
     * 属性选择性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "选择性更新操作", notes="只更新entity中设置为非null的属性")
    @PutMapping("/{id}")
    @ResponseBody
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody Gitaccount entity) {
        //GeneratorModuleController g = new GeneratorModuleController();
        //g.gitConfiguration(entity.getAccount(), entity.getEmail(), entity.getGitpath());
        return doUpdateById(getService(), id, entity);
    }

    @ApiOperation(value = "生成SSH密钥", notes="生成SSH密钥")
    @PostMapping("/createSSHKey")
    @ResponseBody
    public JsonResult<String> createSSHKey(@RequestBody Gitaccount entity) {
        String os = System.getProperty("os.name");
        String result = "";
        if(os.toLowerCase().startsWith("win")) { //windows
            String gitcsshkey = entity.getGitpath()+"\\projects-metadata\\src\\main\\resources\\gitcsshkey.sh";
            String gitsshmess = entity.getGitpath()+"\\projects-metadata\\src\\main\\resources\\gitsshmess.sh";
            boolean success = true;
            Runtime run = Runtime.getRuntime();
            try {
                //Process process1 = run.exec("sh " + gitcsshkey + " " + entity.getGitpath() + " " + entity.getEmail());
                Process process = run.exec("sh " + gitsshmess + " " + entity.getGitpath());
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            String gitcsshkey = entity.getGitpath()+"/test_expect.sh";
            String gitsshmess = entity.getGitpath()+"/gitsshmess.sh";
            boolean success = true;
            Runtime run = Runtime.getRuntime();
            try {
                Process process1 = run.exec(gitcsshkey + " " + entity.getEmail());
                Process process = run.exec(gitsshmess);
                InputStream in = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("GBK")));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    result += line;
                }
                process.waitFor();
                in.close();
                reader.close();
                process.destroy();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return renderSuccess(result);
    }

    /**
     * 全部属性更新操作
     * @param id
     * @param entity
     * @return
     */
    @ApiOperation(value = "执行修改操作", notes="注意必须提供实体的所有属性否则没有提供的属性将被设置为null")
    @PutMapping("/{id}/allColumn")
    @ResponseBody
    public JsonResult<Void> doUpdateAllColumnById(@PathVariable("id") Long id, @RequestBody Gitaccount entity) {

        return doUpdateAllColumnById(getService(), id, entity);
    }


    @ApiOperation(value = "根据id查询", notes = "查看指定id的实体对象")
    @GetMapping("/{id}")
    @ResponseBody
    public JsonResult<Gitaccount> getById(@PathVariable("id") Long id) {
        return doGetById(getService(), id);
    }
    
    @ApiOperation(value = "根据实体属性查询", notes="根据vo指定条件进行查询")
    @PostMapping(value = "/list")
    @ResponseBody
    public JsonResult<Pager<Gitaccount>> getList(@RequestBody BaseVo<Gitaccount> vo){
        return doList(getService(),vo.getPager(),vo.getEntity());
    }


    private GitaccountService getService() {
        return gitaccountService;
    }



}

