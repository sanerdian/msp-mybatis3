package com.jnetdata.msp.cs1.cs.controller;

import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * <p>jar包热加载Controller</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on  下午9:28 2022/11/23
 */
@RestController
@RequestMapping("/hot")
public class HotLoaderController {

    /**主动调用单次jar加载，必传路径*/
    @GetMapping( "loader")
    @ResponseBody
    public String loadJar(@RequestParam(name = "jarPath",required = true) String jarPath) throws Exception {
        File file = new File(jarPath);
        if (file.exists() && file.isFile()){
            HotClassLoader.loadJar(file.getAbsolutePath(),null);
            return null;
        }
        throw new IllegalArgumentException("路径非法，无法正确解析！");
    }

    @GetMapping( "loaders")
    @ResponseBody
    public String loadAllJars() throws Exception {
        HotClassLoader.loadAllJar();
        return null;
    }

    /**固定间隔3秒扫描一次plugins目录，第一次全量加载，其余热替换*/
//    @Scheduled(fixedDelay = 3*1000L)
    public void loadJars() {
        try {
            HotClassLoader.loadAllJar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("plugins scanned done....");
    }

}
