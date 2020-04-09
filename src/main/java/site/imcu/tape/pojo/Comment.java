package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName(value = "tape_comment")
public class Comment extends Base {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String text;
    private Long fromId;
    private Long toId;
    private Long clipId;
    @TableField(exist = false)
    private Integer likedCount;
    @TableField(exist = false)
    private Boolean liked;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Clip clip;
}
