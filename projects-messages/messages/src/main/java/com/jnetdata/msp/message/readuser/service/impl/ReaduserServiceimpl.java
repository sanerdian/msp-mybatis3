package com.jnetdata.msp.message.readuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.message.msg.mapper.MsgMapper;
import com.jnetdata.msp.message.msg.model.Msg;
import com.jnetdata.msp.message.msg.service.MsgService;
import com.jnetdata.msp.message.readuser.mapper.ReaduserMapper;
import com.jnetdata.msp.message.readuser.model.ReaduserModel;
import com.jnetdata.msp.message.readuser.service.ReaduserService;
import org.springframework.stereotype.Service;

@Service
public class ReaduserServiceimpl  extends BaseServiceImpl<ReaduserMapper, ReaduserModel> implements ReaduserService {
    @Override
    public ReaduserModel selectUser(Long userid, Long readid) {
        QueryWrapper<ReaduserModel> readuserModelQueryWrapper = new QueryWrapper<>();
        readuserModelQueryWrapper.eq("userid", userid).eq("readid", readid);
        ReaduserModel readuserModel = baseMapper.selectOne(readuserModelQueryWrapper);
        return readuserModel;
    }
    @Override
    public void upUser(ReaduserModel readmodel) {
        readmodel.setStatus(1);
       baseMapper.updateById(readmodel);
    }
    @Override
    public void insetUser(Long userid,Long readid) {
        ReaduserModel readuserModel = new ReaduserModel();
        readuserModel.setStatus(1);
        readuserModel.setReadid(readid);
        readuserModel.setUserid(userid);
        save(readuserModel);
    }
    /*
    * 更新所有数据全部已读
    * @pram ： （long userid ， long readid）
    * */
    @Override
    public void insetUser1(Long userid,Long readid) {
        ReaduserModel readuserModel = new ReaduserModel();
        readuserModel.setReadid(readid);
        readuserModel.setUserid(userid);
        int insert1 = baseMapper.insert(readuserModel);
    }
}
