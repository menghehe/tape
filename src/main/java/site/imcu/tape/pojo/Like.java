package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: MengHe
 * @date: 2020/3/14 16:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tape_like")
public class Like extends Base {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String type;
    private Integer userId;
    private Integer targetId;
    private User user;
}
