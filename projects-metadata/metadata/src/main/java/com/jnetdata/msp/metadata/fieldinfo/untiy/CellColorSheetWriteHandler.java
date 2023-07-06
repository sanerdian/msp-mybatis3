package com.jnetdata.msp.metadata.fieldinfo.untiy;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.*;
import java.util.HashMap;
import java.util.List;


public class CellColorSheetWriteHandler implements CellWriteHandler {
    /**
     * map
     * key：第i行
     * value：第i行中单元格索引集合
     */
    private HashMap<Integer,List<Integer>> map;

    /**
     * 颜色
     */
    private Short colorIndex;

    /**
     * 有参构造
     */
    public CellColorSheetWriteHandler(HashMap<Integer, List<Integer>> map, Short colorIndex) {
        this.map = map;
        this.colorIndex = colorIndex;
    }

    /**
     * 无参构造
     */
    public CellColorSheetWriteHandler() {

    }

    /**
     * 在创建单元格之前调用
     */
    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
    }

    /**
     * 在单元格创建后调用
     */
    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    /**
     * 在单元上的所有操作完成后调用
     */
    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

        /**
         * 考虑到导出数据量过大的情况，不对每一行的每一个单元格进行样式设置，只设置必要行中的某个单元格的样式
         */
        //当前行的第i列
        int i = cell.getColumnIndex();
        //不处理第一行
            List<Integer> integers = map.get(cell.getRowIndex());
            if (integers != null && integers.size() > 0) {
                if (integers.contains(i)) {
                    // 根据单元格获取workbook
                    Workbook workbook = cell.getSheet().getWorkbook();
                    // 单元格策略
                    WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

                    // 创建字体实例
                    WriteFont cellWriteFont = new WriteFont();
                   /* //设置字体颜色
                    cellWriteFont.setColor(colorIndex);*/
                    // 背景白色
                    contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                    contentWriteCellStyle.setWriteFont(cellWriteFont);
                    CellStyle cellStyle = StyleUtil.buildHeadCellStyle(workbook, contentWriteCellStyle);

                    //设置当前行第i列的样式
                    cell.getRow().getCell(i).setCellStyle(cellStyle);
                }
            }
    }
}







