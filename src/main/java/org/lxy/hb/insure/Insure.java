package org.lxy.hb.insure;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuxinyi
 * @date 2019-08-23
 */
public class Insure {

    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("/Users/liuxinyi/Desktop/insure.xlsx"));
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<String> list = new ArrayList<>(512);
        for (Row row : sheet) {
            String value = row.getCell(0).getStringCellValue();
            if (StringUtils.isNotBlank(value)){
                value = value.trim();
                value = "'" + value + "'";
                list.add(value);
            }
        }
        String collect = list.stream().collect(Collectors.joining(","));
        System.out.println(collect);
    }
}
