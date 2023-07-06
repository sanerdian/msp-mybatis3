package com.jnetdata.msp.media.service.impl;

import com.jnetdata.msp.media.mapper.FznMapper;
import com.jnetdata.msp.media.service.FznService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class FznServiceImpl implements FznService {
    @Autowired
    private FznMapper fznMapper;
    @Override
    public String pagefznStyles(int type, int current, Integer size) throws IOException, SQLException {
        if(size==null||size<0){
            size=35;
        }
        int start=(current-1)*size+1;
        int end=current*size;
        List<Map> maps = fznMapper.pageStyles(type, start, end);
        StringBuffer buffer = new StringBuffer();
        if(maps!=null){
            for(Map map:maps){
                buffer.append("<li class='col-xs-12 brush' data-id='").append(map.get("ID")).append("'>");
                buffer.append(ClobToString((Clob) map.get("CODE")));
                buffer.append("</li>");
            }
        }
        return buffer.toString();
    }
    // 将字CLOB转成STRING类型
    public String ClobToString(Clob clob) throws SQLException, IOException {

        String reString = "";
        java.io.Reader is = clob.getCharacterStream();// 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }
}
