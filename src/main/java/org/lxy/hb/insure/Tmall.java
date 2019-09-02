package org.lxy.hb.insure;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxinyi
 * @date 2019-08-23
 */
public class Tmall {

    public static void main(String[] args) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("/Users/liuxinyi/Desktop/TMALL_PG_Mac(1).xlsm"));
        XSSFSheet sheet0 = workbook.getSheetAt(0);
        printSheet(sheet0, "t_rent_tmall_main_order");
//        XSSFSheet sheet1 = workbook.getSheetAt(1);
//        printSheet(sheet1, "t_rent_tmall_sub_order");

    }

    private static void printSheet(Sheet sheet, String talbeName) {
        List<String> fieldList = new ArrayList<>(256);
        List<String> commentList = new ArrayList<>(256);
        int i = 0;

        for (Row row : sheet) {
            i++;
            if (i < 3) {
                continue;
            }
            String col = row.getCell(0).getStringCellValue();
            String type = row.getCell(1).getStringCellValue().toLowerCase();
            String comment = row.getCell(2).getStringCellValue();
            StringBuilder sb = new StringBuilder();
            sb.append("COMMENT ON COLUMN ")
                    .append(talbeName)
                    .append(".")
                    .append(col)
                    .append(" IS '数据密级C1,")
                    .append(comment)
                    .append("';");
            commentList.add(sb.toString());

            StringBuilder fieldBuilder = new StringBuilder();
            fieldBuilder.append("private ")
                    .append(getJavaType(type))
                    .append(" ")
                    .append(getFieldName(col))
                    .append(";");
            fieldList.add(fieldBuilder.toString());
        }

        for (String s : fieldList) {
            System.out.println(s);
        }

        System.out.println("-----------------------------");

        for (String s : commentList) {
            System.out.println(s);
        }
    }

    private static String getJavaType(String type) {
        if (type.startsWith("varch")) {
            return "String";
        } else if (type.startsWith("big")) {
            return "Long";
        } else if (type.startsWith("boolean")) {
            return "Boolean";
        } else if (type.startsWith("integer")) {
            return "Integer";
        } else if (type.startsWith("time")) {
            return "LocalDateTime";
        } else if (type.startsWith("json")) {
            return "JSONObject";
        }
        throw new RuntimeException("type");
    }

    private static String getFieldName(String col) {
        String[] split = col.split("_");
        if (split.length == 1) {
            return split[0];
        }
        StringBuilder sb = new StringBuilder(split[0]);
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                continue;
            }
            String s = split[i];
            String first = s.substring(0, 1);
            String upper = first.toUpperCase();
            s = s.replaceFirst(first, upper);
            sb.append(s);
        }
        return sb.toString();
    }
}
