package com.jnetdata.msp.media.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jnetdata.msp.media.mapper.JobApiMapper;
import com.jnetdata.msp.media.model.JobApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class JobApiBean {
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private JobApiMapper mapper;
    public static JobApiMapper staticMapper;

    @PostConstruct
    public void init(){
        staticMapper=this.mapper;
    }
    @Scheduled(cron = "0 */1 * * * ?")
    @Async
    public void runJob() {
        PropertyWrapper<JobApi> wrapper=new PropertyWrapper<>(JobApi.class);

        wrapper.lt("jobtime",new java.util.Date());
        wrapper.eq("status",0);
        List<JobApi> jobApis=staticMapper.selectList(wrapper.getWrapper());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        for(JobApi jobApi:jobApis){
            Object body=jobApi.getReqbody();
            HttpEntity entity = new HttpEntity(body,headers);
            HttpMethod httpMethod = null;
            String method=jobApi.getReqmethod();
            if("get".equalsIgnoreCase(method)){
                httpMethod = HttpMethod.GET;
            }else if("delete".equalsIgnoreCase(method)){
                httpMethod = HttpMethod.DELETE;
            }else if("post".equalsIgnoreCase(method)){
                httpMethod = HttpMethod.POST;
            }else if("put".equalsIgnoreCase(method)){
                httpMethod = HttpMethod.PUT;
            }else if("patch".equalsIgnoreCase(method)){
                httpMethod = HttpMethod.PATCH;
            }else if("head".equalsIgnoreCase(method)){
                httpMethod =HttpMethod.HEAD;
            }
            try{
                ResponseEntity<String> exchange = restTemplate.exchange(jobApi.getRequrl(), httpMethod, entity, String.class, body);

                int status=2;
                String result="更新失败";

                if(exchange.getStatusCodeValue()>=200&&exchange.getStatusCodeValue()<300){
                    status=1;
                    result="更新成功！";
                }
                jobApi.setStatus(status);
                jobApi.setResult(result);
            }catch (Exception e){
                jobApi.setStatus(3);
                jobApi.setResult("更新异常");
            }
            staticMapper.updateById(jobApi);
        }
    }
}
