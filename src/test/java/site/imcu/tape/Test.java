package site.imcu.tape;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            ExcelReader reader = ExcelUtil.getReader("C:\\Users\\mengh\\Desktop\\数据库表.xlsx", i);
            List<TableStruct> structs = reader.readAll(TableStruct.class);
            System.out.println("CREATE TABLE tape_" + i + "(");
            for (TableStruct struct : structs) {
                if (struct.get字段类型().equals("varchar")){
                    System.out.println(struct.get字段名称() + " " + struct.get字段类型() + "(255) comment '" + struct.get字段描述() + "',");
                }else {
                    System.out.println(struct.get字段名称() + " " + struct.get字段类型() + " comment '" + struct.get字段描述() + "',");
                }
            }
            System.out.println(")ENGINE=InnoDB DEFAULT CHARSET=utf8;\n");
        }
    }
}
