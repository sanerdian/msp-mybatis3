package com.jnetdata.msp.util;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
/*import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;*/
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wangshuaijun
 * @description
 * 读取文件工具类：支持以下文件内容读取
 * 1. word(.doc),word(.docx)
 * 2. excel(.xls),excel(xlsx)
 * 3. pdf
 * 4. txt
 * 5. ppt(.ppt),pptx(,pptx)
 * @date 2019年4月19日10:52:45
 *
 */
public class ReadFileUtils {


    public static void main(String[] args) {
        String contentByPath = getContentByPath("E:\\upload\\unstructured\\06d7b190-e2ae-4d74-945c-1bfef33fb0f8.docx");
        System.out.println(contentByPath);
    }


    /**
     * 根据文件类型返回文件内容
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    public static String getContentByPath(String filepath) {
        String[] fileTypeArr = filepath.split("\\.");
        String fileType = fileTypeArr[fileTypeArr.length - 1];
        if ("doc".equals(fileType) || "docx".equals(fileType)) {
            return readWord(filepath, fileType);
        } else if ("xlsx".equals(fileType) || "xls".equals(fileType)) {
            return readExcel(fileType, filepath);
        } else if ("txt".equals(fileType)) {
            return readTxt(filepath);
        } else if ("pdf".equals(fileType)) {
            return readPdf(filepath);
        } else if ("ppt".equals(fileType) || "pptx".equals(fileType)) {
            return readPPT(fileType, filepath);
        } else {
            System.out.println("不支持的文件类型！");
        }
        return "";
    }

    /**
     * 读取PDF中的内容
     *
     * @param filePath
     * @return
     */
    public static String readPdf(String filePath) {
        FileInputStream fileInputStream = null;
        PDDocument pdDocument = null;
        String content = "";
        try {
            //创建输入流对象
            fileInputStream = new FileInputStream(filePath);
            //创建解析器对象
            PDFParser pdfParser = new PDFParser(new RandomAccessBuffer(fileInputStream));
            pdfParser.parse();
            //pdf文档
            pdDocument = pdfParser.getPDDocument();
            //pdf文本操作对象,使用该对象可以获取所读取pdf的一些信息
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            content = pdfTextStripper.getText(pdDocument);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //PDDocument对象时使用完后必须要关闭
                if (null != pdDocument) {
                    pdDocument.close();
                }
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读取Excel中的内容
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    private static String readTxt(String filePath) {
        File f = new File(filePath);
        try {
            return FileUtils.readFileToString(f, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取Excel中的内容
     *
     * @param filePath
     * @return
     */
    private static String readExcel(String fileType, String filePath) {

        try {
            File excel = new File(filePath);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(fileType)) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(fileType)) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    System.out.println("文件类型错误!");
                    return "";
                }
                //开始解析,获取页签数
                StringBuffer sb = new StringBuffer("");
                for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                    Sheet sheet = wb.getSheetAt(i);     //读取sheet
                    sb.append(sheet.getSheetName() + "_");
                    int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                    int lastRowIndex = sheet.getLastRowNum();
                    for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                        Row row = sheet.getRow(rIndex);
                        if (row != null) {
                            int firstCellIndex = row.getFirstCellNum();
                            int lastCellIndex = row.getLastCellNum();
                            for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                                Cell cell = row.getCell(cIndex);
                                if (cell != null) {
                                    sb.append(cell.toString());
                                }
                            }
                        }
                    }
                }
                return sb.toString();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取word中的内容
     *
     * @param path
     * @param fileType
     * @return
     */
    public static String readWord(String path, String fileType) {
        String buffer = "";
        try {
            InputStream is = new FileInputStream(path);
            if ("doc".equals(fileType)) {
                buffer = readWordDoc(is);
            } else if ("docx".equals(fileType)) {
                try {
                    XWPFDocument doc = new XWPFDocument(is);
                    XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
                    buffer = extractor.getText();
                    extractor.close();
                }catch (Exception e){
                    e.printStackTrace();
                    InputStream iis = new FileInputStream(path);
                    buffer = readWordDoc(iis);
                }
            } else {
                System.out.println("此文件不是word文件！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer;
    }

    private static String readWordDoc(InputStream is) throws IOException {
        WordExtractor ex = new WordExtractor(is);
        String buffer = ex.getText();
        ex.close();
        return buffer;
    }

    private static String readPPT(String fileType, String filePath) {
        try {
            if ("ppt".equals(fileType)) {
                PowerPointExtractor extractor = new PowerPointExtractor(new FileInputStream(new File(filePath)));
                return extractor.getText();
            } else if ("pptx".equals(fileType)) {
                return new XSLFPowerPointExtractor(POIXMLDocument.openPackage(filePath)).getText();
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        } catch (XmlException e) {
            e.getMessage();
        } catch (OpenXML4JException e) {
            e.getMessage();
        }

        return "";
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
