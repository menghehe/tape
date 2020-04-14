package site.imcu.tape.uitls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.Like;
import site.imcu.tape.service.impl.CommentServiceImpl;
import site.imcu.tape.service.impl.LikeServiceImpl;

import java.util.List;
import java.util.Set;

/**
 * @author MengHe
 * @date 2020/4/9 8:42
 */
@Component
@Slf4j
public class RedisSync {
    @Autowired
    LikeServiceImpl likeService;
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    RedisKey redisKey;
    @Autowired
    RedisUtil redisUtil;

    @EventListener(ApplicationReadyEvent.class)
    public void sync(){
        Set<String> keys = redisUtil.keys("*");
        redisUtil.delete(keys);
        List<Like> likeList = likeService.getAll();
        for (Like like : likeList) {
            if (like.getLikeType()==1){
                String userLikedClipKey = redisKey.userLikedClip(like.getFromId(), like.getTargetId());
                redisUtil.set(userLikedClipKey, "true");
                String clipLikedCount = redisKey.clipLikedCount(like.getTargetId());
                redisUtil.incrBy(clipLikedCount, 1);
            }
            if(like.getLikeType()==2){
                String userLikedComment = redisKey.userLikedComment(like.getFromId(), like.getTargetId());
                redisUtil.set(userLikedComment, "true");
                String commentLikedCount = redisKey.commentLikedCount(like.getTargetId());
                redisUtil.incrBy(commentLikedCount, 1);
            }
        }

        List<Comment> commentList = commentService.getAll();
        for (Comment comment : commentList) {
            String clipCommentCount = redisKey.clipCommentCount(comment.getClipId());
            redisUtil.incrBy(clipCommentCount, 1);
        }


    }
}
