package com.jnetdata.msp.metadata.fieldinfo.untiy;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import dm.jdbc.util.StringUtil;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelFillCellMergePrevCol implements CellWriteHandler{
    private static final String KEY ="%s-%s";
    //所有的合并信息都存在了这个map里面
    Map<String, Integer> mergeInfo = new HashMap<>();

    public ExcelFillCellMergePrevCol() {
    }
    public ExcelFillCellMergePrevCol(Map<String, Integer> mergeInfo) {
        this.mergeInfo = mergeInfo;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        //当前行
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();

        Integer num = mergeInfo.get(String.format(KEY, curRowIndex, curColIndex));
        if(null != num){
            // 合并最后一行 ,列
            mergeWithPrevCol(writeSheetHolder, cell, curRowIndex, curColIndex,num);
        }
    }

    public void mergeWithPrevCol(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex, int num) {
        Sheet sheet = writeSheetHolder.getSheet();
        CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex, curRowIndex, curColIndex, curColIndex + num);
        sheet.addMergedRegion(cellRangeAddress);
    }

    //num从第几列开始增加多少列
    // curRowIndex 在第几行进行行合并
    // curColIndex 在第几列进行合并
    // num 合并多少格
    // 比如我上图中中心需要在第三行 从0列开始合并三列 所以我可以传入 (3,0,2)
    public void add (int curRowIndex,  int curColIndex , int num){
        mergeInfo.put(String.format(KEY, curRowIndex, curColIndex),num);
    }
}
