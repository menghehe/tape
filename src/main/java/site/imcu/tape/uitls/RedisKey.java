package site.imcu.tape.uitls;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/**
 * @author MengHe
 * @date 2020/4/8 9:25
 */
@Component
public class RedisKey {

    public String userLikedClip(Long userId,Long clipId){
        return StrUtil.format("user:{}:like:clip:{}", userId,clipId);
    }

    public String userLikedComment(Long userId,Long commendId){
        return StrUtil.format("user:{}:like:comment:{}", userId,commendId);
    }

    public String clipLikedCount(Long clipId){
        return StrUtil.format("clip:{}:likedCount", clipId);
    }

    public String clipCommentCount(Long clipId){
        return StrUtil.format("clip:{}:commentCount", clipId);
    }

    public String commentLikedCount(Long commentId){
        return StrUtil.format("comment:{}:likedCount", commentId);
    }

    public String clipHeat(){return "clipHeat";}
}
