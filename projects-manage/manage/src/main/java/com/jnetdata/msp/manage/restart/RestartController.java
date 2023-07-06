package com.jnetdata.msp.manage.restart;

import cn.hutool.core.io.FileUtil;
import com.jnetdata.msp.core.controller.BaseController;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.member.limit.service.PermissionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thenicesys.web.JsonResult;
import java.io.File;

@Controller
@RequestMapping("/manage")
public class RestartController extends BaseController {

    @Autowired
    PermissionService permissionService;

    @GetMapping("restartServer")
    @ResponseBody
    public JsonResult restartServer(){
//        permissionService.checkPermitted("manage:server:restart");
        restart();
        return renderSuccess();
    }

    protected void restart() {
        if(FileUtil.isWindows()){
            restartWindows();
        }else{
            restartLinux();
        }

    }

    @SneakyThrows
    private void restartWindows(){
        String tomcat = System.getProperty("catalina.home");
        String java = System.getProperty("java.home");
        java = java.substring(0, java.lastIndexOf("\\"));
        String filePath = tomcat + "\\bin\\restart.bat";
        if(!FileUtil.exist(filePath)){
            String str = "@echo off\n" +
                    "call shutdown.bat\n" +
                    "call startup.bat\n" +
                    "exit\n";
            FileUtil.writeString(str,filePath,"utf-8");
        }
        String[] envp = { "JAVA_HOME=" + java, "CATALINA_HOME=" + tomcat };
        Runtime.getRuntime().exec("cmd /c start " + filePath, envp, new File(tomcat + File.separator + "bin"));
    }

    @SneakyThrows
    private void restartLinux(){
        String tomcat = System.getProperty("catalina.home");
        String java = System.getProperty("java.home");
        java = java.substring(0, java.lastIndexOf("/"));
        String filePath = tomcat + "/bin/restart.sh";
        if(!FileUtil.exist(filePath)){
            String str = "sh shutdown.sh\n" +
                    "sh startup.sh\n" +
                    "exit\n";
            FileUtil.writeString(str, filePath,"utf-8");
        }
        String[] envp = { "JAVA_HOME=" + java, "CATALINA_HOME=" + tomcat };
        Runtime.getRuntime().exec(filePath, envp, new File(tomcat + File.separator + "bin"));
    }
}
