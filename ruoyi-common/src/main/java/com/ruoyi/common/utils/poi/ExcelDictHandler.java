package com.ruoyi.common.utils.poi;

import com.ruoyi.common.utils.DictUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 老版若依 Excel 字典转换处理器
 * 导出：字典值 → 中文标签
 */
public class ExcelDictHandler implements ExcelHandlerAdapter {

    /**
     * 格式化单元格值
     * @param value 字段值（数据库存的字典值：0,1,2...）
     * @param args  注解中的 args = {"process_status"}
     * @param cell  当前单元格
     * @param wb    工作簿
     * @return 转换后的中文
     */
    @Override
    public Object format(Object value, String[] args, Cell cell, Workbook wb) {

        if (args == null || args.length == 0) {
            return value;
        }

        String dictType = args[0];

        if (value == null) {
            return "未知";
        }

        return DictUtils.getDictLabel(dictType, value.toString(), "未知");
    }
}
