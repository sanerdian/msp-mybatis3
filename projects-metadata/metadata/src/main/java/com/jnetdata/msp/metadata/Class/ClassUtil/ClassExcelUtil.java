package com.jnetdata.msp.metadata.Class.ClassUtil;

import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.metadata.Class.model.Class;
import com.jnetdata.msp.metadata.Class.service.ClassService;
import lombok.val;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ClassExcelUtil {

    @Autowired
    private ClassService service;

    /**
     * 下载数据到excel
     * @param os
     * @param classList
     * @param sheetName
     * @param title
     * @param wb
     */
    public static void doExport(ServletOutputStream os, List<Class> classList, String sheetName, String[] title, HSSFWorkbook wb) {

        //1.创建HssfWorkBook,对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        //2.创建sheet对象
        HSSFSheet sheet = wb.createSheet(sheetName);

        //3.在sheet表头中添加第0行
        HSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < title.length; i++) {
            titleRow.createCell(i).setCellValue(title[i]);

        }
        //创建内容
        int startRow = 1;
        for (Class c : classList) {
            HSSFRow row = sheet.createRow(startRow);
            row.createCell(0).setCellValue(c.getClassName());
            row.createCell(1).setCellValue(c.getClassDesc());
            row.createCell(2).setCellValue(c.getClassCode());
            row.createCell(3).setCellValue(c.getParentId());
            if(c.getCreateTime()!=null)row.createCell(4).setCellValue(c.getCreateTime());
            row.createCell(5).setCellValue(c.getCrUser());
            if(c.getModifyTime()!=null) row.createCell(6).setCellValue(c.getModifyTime());
            row.createCell(7).setCellValue(c.getModifyUser());
            startRow = startRow + 1;
        }

        try {
            wb.write(os);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 导入Excel数据
     * @param mFile
     * @return
     */
    public static List<Class> importExcel(MultipartFile mFile) {

        HSSFWorkbook wb = null;
        List<Class> classList = new ArrayList<>();
        try {
            val user = SessionUser.getCurrentUser();
            BufferedInputStream in = new BufferedInputStream(mFile.getInputStream());
            POIFSFileSystem fs = new POIFSFileSystem(in);
            wb = new HSSFWorkbook(fs);
            //获取Sheet
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet sheet = wb.getSheetAt(sheetIndex);
                //提取数据
                for (int i = 1; i < sheet.getLastRowNum()+1; i++) {
                    HSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    Class c = new Class();
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        HSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            if (j == 0) {
                                c.setClassName(cell.getStringCellValue());
                            } else if (j == 1) {
                                c.setClassDesc(cell.getStringCellValue());
                            }
                            c.setParentId(0l);
                        }
                    }
                    classList.add(c);
                }
            }
            return classList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导入分类法的分类
     * @param id
     * @param mFile
     * @return
     */
    public static List<Class> getExcCategory(Long id, MultipartFile mFile) {
        HSSFWorkbook wb = null;
        List<Class> classList = new ArrayList<>();
        try {
            BufferedInputStream bs = new BufferedInputStream(mFile.getInputStream());
            POIFSFileSystem ps = new POIFSFileSystem(bs);
            wb = new HSSFWorkbook(ps);
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet sheet = wb.getSheetAt(sheetIndex);
                for (int rowIndex = 1; rowIndex < sheet.getLastRowNum()+1; rowIndex++) {
                    HSSFRow row = sheet.getRow(rowIndex);
                    if (row == null) {
                        continue;
                    }
                    Class c = new Class();
                    for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                        HSSFCell cell = row.getCell(cellIndex);
                        if (cellIndex == 0) {
                            c.setClassName(cell.getStringCellValue());
                        } else if (cellIndex == 1) {
                            c.setClassDesc(cell.getStringCellValue());
                        } else if (cellIndex == 2) {
                            c.setClassCode(cell.getStringCellValue());
                        }

                    }
                    c.setParentId(id);
                    classList.add(c);
                }
            }
            return classList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
