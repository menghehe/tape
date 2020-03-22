package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
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
    private Integer id;
    private String title;
    private String clipPath;
    private String sourceFile;
    private String thumbnail;
    private Integer creator;
    private Boolean transcoded;
    private User user;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean liked;

}
