package com.jnetdata.msp.dict3.wordname.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {


    @ApiOperation(value = "问好", notes="问个好")
    @GetMapping("/hey")
    @ResponseBody
    @RequestMapping(value = {"/hey"},method = RequestMethod.GET)
    public String hey(){
        return "index";
    }
}
