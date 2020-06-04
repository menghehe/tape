package site.imcu.tape.pojo;

import lombok.Builder;
import lombok.Data;


/**
 * @author mengh
 */
@Builder
@Data
public class ResponseData {
    private Integer code;
    private String message;
    public Object data;
}