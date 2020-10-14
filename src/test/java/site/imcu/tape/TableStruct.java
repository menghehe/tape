package site.imcu.tape;

import lombok.Data;

@Data
public class TableStruct {
    private String 字段名称;
    private String 字段描述;
    private String 字段类型;
    private String 允许空;
}
