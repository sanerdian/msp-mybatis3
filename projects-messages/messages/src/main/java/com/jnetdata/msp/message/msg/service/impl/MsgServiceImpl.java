package com.jnetdata.msp.message.msg.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.group.model.Groupinfo;
import com.jnetdata.msp.member.group.service.GroupService;
import com.jnetdata.msp.member.role.model.Role;
import com.jnetdata.msp.member.role.service.RoleService;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.service.UserService;
import com.jnetdata.msp.message.msg.entity.MsgVoEntity;
import com.jnetdata.msp.message.msg.mapper.MsgMapper;
import com.jnetdata.msp.message.msg.model.Msg;
import com.jnetdata.msp.message.msg.model.MsgModel;
import com.jnetdata.msp.message.msg.model.WxTemplateMsg;
import com.jnetdata.msp.message.msg.service.MsgService;
import com.jnetdata.msp.message.msg.utils.HttpClientUtils;
import com.jnetdata.msp.message.msgConfig.service.SetMsgService;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import javax.servlet.ServletOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by TF on 2019/3/14.
 */
@Service
public class MsgServiceImpl extends BaseServiceImpl<MsgMapper,Msg> implements MsgService {

    @Autowired
    private SetMsgService setMsgService;


    @Autowired
    private UserService userService;

    /**
     * 模糊查询
     * @param condition
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    protected PropertyWrapper<Msg> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("receptionId")
                .eq("sendID")
                .like("content")
                .eq("isDisplay")
                .eq("type")
                .eq("parentId")
                .eq("addressType")
                .like("title")
                .eq("configId")
                .eq("status")
                .like("crUser")
                .getWrapper();
    }

    /**
     * 微信公众号发送模板消息
     * @param msg  前端传递的数据
     * @param userList1  消息接收者的openId
     * @param WxNewsId  消息日志id
     */
    public void extracted(Msg msg, List<User> userList1, Long WxNewsId) {
        com.jnetdata.msp.message.msgConfig.model.Msg configMsg = setMsgService.get(new PropertyWrapper<>(com.jnetdata.msp.message.msgConfig.model.Msg.class)
                .eq("configId", msg.getConfigId()).select("accessKeyId", "secretAccessKey","templateId"));

        String content = msg.getContent().substring(3, msg.getContent().length()-4);
        String title = msg.getTitle();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String tim = df.format(new Date());//将日期转换字符串

        MsgModel msgModel = new MsgModel();
        msgModel.setKeyword1(title);
        msgModel.setKeyword2(content);
        String name = SessionUser.getCurrentUser().getName();
        msgModel.setKeyword3(name);
        msgModel.setKeyword4(tim);
        String downUrl="http://192.168.8.171/wxIndex.html?id="+ WxNewsId +"&time="+tim;
        msgModel.setJumpUrl(downUrl);

        msgModel.setAccessKeyId(configMsg.getAccessKeyId());
        msgModel.setSecretAccessKey(configMsg.getSecretAccessKey());
        msgModel.setTemplateId(configMsg.getTemplateId());

        if (!userList1.isEmpty()){
            userList1.stream().forEach(m -> {
                String openidWx = m.getOpenidWx();
                msgModel.setOpenId(openidWx);
                sendTemplateMsg(msgModel);
            });
        }
    }

    /**
     * 微信公众号获取获取token
     * @return
     */
    public String getWeiXinToken(String appId,String appSecret){
        String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ appId + "&secret=" + appSecret;
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        JSONObject jsonObj = new JSONObject(forObject);
        return jsonObj.get("access_token").toString();
    }

    /**
     * 模板消息封装
     * @param
     * @return
     */
    public String noticeTemplate(MsgModel model) {
        // 模版消息ID
        String openIdd=model.getOpenId();
        //String templateId="Bw89odTrknc4nmYaLpBa8CfbIpRNzL_lTi44Qki9oUA";
        String templateId=model.getTemplateId();
        TreeMap<String, TreeMap<String, String>> params = new TreeMap<>();
        //根据具体模板参数组装
        params.put("keyword1", WxTemplateMsg.item(model.getKeyword1(), "#173177"));
        params.put("keyword2", WxTemplateMsg.item(model.getKeyword2(), "#173177"));
        params.put("keyword3", WxTemplateMsg.item(model.getKeyword3(), "#173177"));
        params.put("keyword4", WxTemplateMsg.item(model.getKeyword4(), "#173177"));
        //params.put("expDate", WxTemplateMsg.item(model.getKeyword3(), "#173177"));
        WxTemplateMsg wxTemplateMsg = new WxTemplateMsg();
        // 模版ID
        wxTemplateMsg.setTemplate_id(templateId);
        // 消息接收者openId
        wxTemplateMsg.setTouser(openIdd);
        // 关键字赋值
        wxTemplateMsg.setData(params);
        // 模板跳转链接
        wxTemplateMsg.setUrl(model.getJumpUrl());
        String data = JSONUtil.toJsonStr(wxTemplateMsg);
        return data;
    }

    /**
     * 发送模板消息
     */
    @SneakyThrows
    public void sendTemplateMsg(MsgModel model){
        //获取token
        String weiXinToken = getWeiXinToken(model.getAccessKeyId(),model.getSecretAccessKey());
        String data = noticeTemplate(model);

        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + weiXinToken;
        //发送消息
        String string = HttpClientUtils.sendPostJsonStr(postUrl, data);
        com.alibaba.fastjson.JSONObject result = JSON.parseObject(string);
        int errcode = result.getIntValue("errcode");

        if(errcode==0){
            System.out.println("模板消息发送成功==========");
        }else{
            System.out.println("模板消息发送失败==========");
        }
    }

    /**
     * 更新信息状态（未读，已读）
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public boolean setRead(Long[] ids) {
        List<Msg> list = new ArrayList<>();
        for(Long id : ids){
            Msg msg = new Msg();
            msg.setId(id);
            msg.setStatus(1);
            list.add(msg);
        }
        return super.updateBatchById(list);
    }

    /**
     * 下载信息数据到excel
     * @param os
     * @param msgList
     * @param title
     * @param sheetName
     * @date 2020/5/26
     * @return
     */
    @Override
    public void export(ServletOutputStream os, List<Msg> msgList, String[] title, String sheetName) {
        HSSFWorkbook wb=null;
        try {
            wb=new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);

            HSSFRow titleRow = sheet.createRow(0);

            HSSFCellStyle style=wb.createCellStyle();
            //style.setAlignment(HorizontalAlignment.CENTER);

            //设置标题
            for (int i=0;i<title.length;i++){
                sheet.setColumnWidth(i,512*10);
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(title[i]);
            }

            //填充数据
            int rowIndex=1;
            for (Msg msg : msgList){

                HSSFRow row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(msg.getTitle());
                row.createCell(1).setCellValue(msg.getContent());
                row.createCell(2).setCellValue(msg.getReceptUserName());
                row.createCell(3).setCellValue(msg.getSendUserName());
                row.createCell(4).setCellValue(msg.getSendTime());
//                row.createCell(0).setCellValue(msgConfig.getId());
//                row.createCell(1).setCellValue(msgConfig.getType());
//                row.createCell(2).setCellValue(msgConfig.getBusType());
//                row.createCell(3).setCellValue(msgConfig.getTitle());
//                row.createCell(4).setCellValue(msgConfig.getReceptionId());
//                row.createCell(5).setCellValue(msgConfig.getSendID());
//                row.createCell(6).setCellValue(msgConfig.getSendState());
//                row.createCell(7).setCellValue(msgConfig.getSendTime());
//                row.createCell(8).setCellValue(msgConfig.getContent());
//                row.createCell(9).setCellValue(msgConfig.getStatus());
                rowIndex++;
            }

            wb.write(os);
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Autowired
    private GroupService groupService;

    @Autowired
    private RoleService roleService;

    /**
     * 获取所有id
     * @param userId
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    @Override
    public boolean setAllRead(Long userId) {
        return baseMapper.setAllRead(userId);
    }

    /**
     * 获取消息数据列表
     * @author 纪凯静
     * @date 2022/9/23
     * @return
     */
    @Override
    public List<Msg> getMsgDataList() {
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        //根据条件进行查找
        QueryWrapper<Msg> msgQueryWrapper = new QueryWrapper<>();
        msgQueryWrapper.eq("parentId",0L).eq("ISDISPLAY",1).eq("SENDER",userId).orderBy(true,false,"SENDTIME");
        List<Msg> records = baseMapper.selectList(msgQueryWrapper);
        // HashMap<Long, Long> hashMap = new HashMap<>();
        records.forEach(s->{
            Long id = s.getId();
            QueryWrapper<Msg> msgQueryWrapper1 = new QueryWrapper<>();
            msgQueryWrapper1.eq("PARENTID",id);
            //用户查找
            List<Msg> msgs = baseMapper.selectList(msgQueryWrapper1.eq("ISDISPLAY",1).eq("ADDRESSTYPE",0).isNotNull("ADDRESSEE"));
            List<String> strings = new ArrayList<>();
            msgs.forEach(w->{
                Long id1 = w.getReceptionId();
                Groupinfo byId = groupService.getById(id1);
                if(byId!=null) {
                    strings.add(byId.getName());
                  //  s.setRoleids(byId.getName());
                }
            });
            s.setRoleids(strings.toString().substring(1,strings.toString().length()-1));
            strings.clear();
            //组织查找
            List<Msg> msgs2 = baseMapper.selectList( new QueryWrapper<Msg>().eq("PARENTID",id).eq("ISDISPLAY",1).eq("ADDRESSTYPE",1));
            msgs2.forEach(u->{
                Long id1 = u.getReceptionId();
                Role byId =roleService.getById(id1);
                //Role byId = roleService.selectId(id1);
                if(byId!=null) {
                    strings.add(byId.getName());
                }
            });
            s.setGrpids(strings.toString().substring(1, strings.toString().length()-1));
            strings.clear();
           //用户查找
            List<Msg> msgs3 = baseMapper.selectList(new QueryWrapper<Msg>().eq("PARENTID",id).eq("ISDISPLAY",3).eq("ADDRESSTYPE",2).isNotNull("ADDRESSEE"));
            msgs3.forEach(u->{
                Long id1 = u.getReceptionId();
                User byId =userService.getById(id1);
                //Role byId = roleService.selectId(id1);
                if(byId!=null) {
                    //stringg.add(byId.getName());
                    strings.add(byId.getName());
                }
            });
           // s.setUserids(stringg.toString().substring(1,stringg.toString().length()-1));
            s.setUserids(strings.toString().substring(1,strings.toString().length()-1));
        });
        //查询消息类型
        List<Long> configIds = records.stream().map(Msg::getConfigId).collect(Collectors.toList());
        Map<Long, String> configNames = new HashMap<>();
        if(configIds.size()>0){
            List<com.jnetdata.msp.message.msgConfig.model.Msg> configs = setMsgService.listByIds(configIds);
            if(configs!=null){
                configNames = configs.stream().collect(Collectors.toMap(m->m.getId(),m->m.getName()));
            }
        }
        Map<Long,String> finalConfigNames= configNames;
        records.forEach(s->s.setMsgSign(finalConfigNames.get(s.getConfigId())));

        return records;

       /* //创建ExcelDictDTO列表，将数据库中的 Dict列表 转换成 ExcelDictDTO列表
        ArrayList<MsgEntity> getMsgDataList = new ArrayList<>(records.size());

        records.forEach(dict -> {
            MsgEntity excelDictDTO = new MsgEntity();
            BeanUtils.copyProperties(dict, excelDictDTO);
            getMsgDataList.add(excelDictDTO);
        });*/

       // return getMsgDataList;
    }

    /**
     * 查看消息
     * @param msgVoEntity
     * @author 纪凯静
     * @date 2023/2/23
     * @return
     */
    public List<Msg> getMsgExe(MsgVoEntity msgVoEntity){
        Page<Msg> page = new Page<>(1,10);
        page = baseMapper.Userpagename(page,msgVoEntity);
        page.getRecords().forEach(s1-> {
            s1.setSendUserName(msgVoEntity.getName());
            Msg msg1 = baseMapper.selectById(s1.getParentId());
            if (msg1 != null) {
                s1.setTitle(msg1.getTitle());
                //s.setContent(msg1.getContent());
            }});
        return page.getRecords();
    }

    /**
     * 获取用户消息
     * @author 纪凯静
     * @date 2023/9/23
     * @return
     */
    @Override
    public List<Msg> getMsgList() {
        //获取用户
        val user = SessionUser.getCurrentUser();
        Long userId = user.getId();
        //根据条件查询
        QueryWrapper<Msg> msgQueryWrapper = new QueryWrapper<>();
        msgQueryWrapper.eq("ADDRESSEE",userId).eq("ISDISPLAY",-1).orderBy(true,false,"SENDTIME").isNotNull("crUser");

        //查询标题和发件人
        List<Msg> msgs = baseMapper.selectList(msgQueryWrapper);
        msgs.forEach(s-> {
            String name = userService.getById(s.getSendID()).getName();
            s.setSendUserName(name);
            Msg msg1 = baseMapper.selectById(s.getParentId());
            if (msg1 != null) {
                s.setTitle(msg1.getTitle());
                //s.setContent(msg1.getContent());
            }});
        return msgs;
    }
}
