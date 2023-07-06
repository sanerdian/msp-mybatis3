package com.jnetdata.msp.media.controller;

import com.jnetdata.msp.media.service.FznService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/fzn")
@Api(description = "非找你")
public class FznController {
    @Autowired
    private FznService fznService;

    @ApiOperation(value = "获取非找你式样编辑器的式样列表", notes="参照非找你编辑器的api.php")
    @GetMapping(value = "fznstyle")
    public ResponseEntity getStyles(int type, int page, Integer size) throws IOException, SQLException {
        String styles=fznService.pagefznStyles(type,page,size);

        return ResponseEntity.ok().header("Content-Type", "text/plain").body(styles);
    }
}
