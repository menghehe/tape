package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
* @author: MengHe
* @date: 2020/3/27 13:17
*/
@Data
@EqualsAndHashCode(callSuper=true)
@TableName(value = "tape_friend")
public class Friend extends Base {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 粉丝
    */
    private Long follower;

    /**
    * 被关注
    */
    private Long following;

}