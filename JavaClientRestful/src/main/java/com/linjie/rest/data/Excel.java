package com.linjie.rest.data;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * @Title：Excel
 * @Package：com.linjie.rest.data
 * @Description：
 * @author：done
 * @date：2021/8/19 9:25
 */
public class Excel {

    public List<List<String>> readExcel(File file) throws Exception{
        // 创建输入流，读取Excel
        FileInputStream is = new FileInputStream(file.getAbsoluteFile());
        // jxl提供的workbook类
        Workbook wb = Workbook.getWorkbook(is);
        // 只有一个sheet直接处理，创建sheet对象
        Sheet sheet = wb.getSheet(0);
        // 得到所有的行数
        int rows = sheet.getRows();
        // 所有数据
        List<List<String>> allData = new ArrayList<>();
        // 越过第一行
        for (int i=1; i<rows; i++){
            List<String> oneData = new ArrayList<>();
            // 得到每一行的单元格数据
            Cell[] cells = sheet.getRow(i);
            for (int j=1; j<cells.length-5; j++){
                oneData.add(cells[j].getContents().trim());
            }
            // 存储每一条数据
            allData.add(oneData);
//            System.out.println(oneData);
        }
        return allData;
    }

}
