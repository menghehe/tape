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
@TableName(value = "tape_comment")
public class Comment extends Base {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String text;
    private Integer userId;
    private Integer clipId;
    private Integer likedCount;
    private Boolean liked;
    private User user;
}
