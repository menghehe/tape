package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author mengh
 */
@Data
public class Base {

    private Date createTime;
    private Integer createMan;
    private Date updateTime;
    private Integer updateMan;
    @TableField(value = "is_deleted")
    private Integer deleted;

    @TableField(exist = false)
    private Integer current;
    @TableField(exist = false)
    private Integer size;
}
