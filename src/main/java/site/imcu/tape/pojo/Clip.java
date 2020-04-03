package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: MengHe
 * @date: 2020/2/22 17:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tape_clip")
public class Clip extends Base {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String clipPath;
    private String coverPath;
    private String sourceFile;
    private Long creator;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Integer likeCount;
    @TableField(exist = false)
    private Integer commentCount;
    @TableField(exist = false)
    private Boolean liked;

}
