package com.jnetdata.msp.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.jnetdata.msp.core.util.ExcelBean;
import com.jnetdata.msp.core.util.ExcelUtil;
import com.jnetdata.msp.core.util.MapUtil;
import com.jnetdata.msp.util.model.Fieldinfo;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.net.URLEncoder;
import java.util.*;

public class DownMetadataUtil {
    public static void downMetadata(List<Fieldinfo> list, List datalist, HttpServletResponse response, String ext, int includeTitle){
        downMetadata(list,datalist,response,ext,includeTitle,null);
    }

    public static void downMetadata(List datalist, HttpServletResponse response, String ext, int includeTitle, Class clz, List<String> fieldNames){
            downExcel(includeTitle, datalist, response, clz,fieldNames);
    }

    public static void downMetadata(List<Fieldinfo> list, List datalist, HttpServletResponse response, String ext, int includeTitle, Class clz){
        if(ext.equals("excel")) {
            if (clz != null) {
                downExcel(list, includeTitle, datalist, response, clz);
            } else {
                downExcel_old(list, includeTitle, datalist, response);
            }
        }else if(ext.equals("txt"))
            downTxt(list,includeTitle,datalist,response);
        else if(ext.equals("dat"))
            downDat(list,includeTitle,datalist,response);
    }

    public static void downTxt(List<Fieldinfo> list,int includeTitle, List datalist, HttpServletResponse response){
        String text = getText(list,includeTitle,datalist);
        //设置响应的内容类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition","attachment;filename=datas.txt");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(text.toString().getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            //LOGGER.error("导出文件文件出错:{}",e);
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                //LOGGER.error("关闭流对象出错 e:{}",e);
            }
        }
    }

    public static void downDat(List<Fieldinfo> list,int includeTitle, List datalist, HttpServletResponse response){
        String text = getText(list,includeTitle,datalist);
        //设置响应的内容类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition","attachment;filename=datas.dat");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(text.toString().getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            //LOGGER.error("导出文件文件出错:{}",e);
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                //LOGGER.error("关闭流对象出错 e:{}",e);
            }
        }
    }

    public static String getText(List<Fieldinfo> list,int includeTitle, List datalist){
        StringBuffer text = new StringBuffer();
        if(includeTitle == 1){
            for (Fieldinfo fieldinfo : list) {
                text.append("\""+fieldinfo.getAnothername()+"\"");
                text.append("\t");
            }
            text.append("\r\n");
        }
        List<Map<String, Object>> maps = MapUtil.toListMap(datalist);
        for (Map<String, Object> map : maps) {
            for(Fieldinfo fieldinfo : list){
                text.append("\""+map.get(fieldinfo.getProName())+"\"");
                text.append("\t");
            }
            text.append("\r\n");
        }
        return text.toString();
    }

    public static void downExcel_old(List<Fieldinfo> list,int includeTitle, List datalist, HttpServletResponse response){
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
        for (Fieldinfo fieldinfo : list) {
            excel.add(new ExcelBean(fieldinfo.getAnothername(), fieldinfo.getProName(), 0));
        }
        map.put(0, excel);
        String sheetName = "datas";  //sheet名
        String excelName = sheetName + ".xlsx";
        try {
            XSSFWorkbook workbook = ExcelUtil.createExcelFile(datalist, map, sheetName);
            response.reset();
            response.setHeader("content-disposition", "attachement;fileName=" + (new String(excelName.getBytes(), "ISO-8859-1")));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downExcel(List<Fieldinfo> list, int includeTitle, List datalist, HttpServletResponse response,Class clz){
        List<String> fieldNames = new ArrayList<>();
        for (Fieldinfo fieldinfo : list) {
            fieldNames.add(fieldinfo.getProName());
        }
        downExcel(includeTitle,datalist,response,clz,fieldNames);
    }

    public static void downExcel(int includeTitle, List datalist, HttpServletResponse response,Class clz,List<String> fieldNames){
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("datas", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clz).sheet("datas").registerConverter(new LongStringConverter()).needHead(includeTitle == 1).includeColumnFiledNames(fieldNames).doWrite(datalist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
