package com.jnetdata.msp.media.task;

import com.jnetdata.msp.tlujy.yjfk.mapper.YjfkMapper;
import com.jnetdata.msp.tlujy.yjfk.model.Yjfk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class yjfkTask {

    @Autowired
    private YjfkMapper yjfkMapper;;

    SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "0 1 * * * ?")
    @Async
    public void jyfkSynchro() {

        Date date= new Date();

        PropertyWrapper wrapper = new PropertyWrapper(Yjfk.class);

        wrapper.eq("state","0");
        wrapper.between("timingdate",new Date(System.currentTimeMillis()-(24*60*60*1000)),date);

        List<Yjfk> list = yjfkMapper.selectList(wrapper.getWrapper());

        for (Yjfk yjfk : list) {

            yjfk.setState(1L);

            yjfkMapper.updateById(yjfk);
        }
    }

}
