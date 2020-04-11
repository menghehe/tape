package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author mengh
 */
@Data
public class Base {

    private Date createTime;
    private Long createMan;
    private Date updateTime;
    private Long updateMan;
    @TableField(value = "is_deleted")
    private Integer deleted;

    @TableField(exist = false)
    private Integer current;
    @TableField(exist = false)
    private Integer size;

    @TableField(exist = false)
    private List<OrderItem> orders;
}

