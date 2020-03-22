package site.imcu.tape.service.impl;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.imcu.tape.mapper.LikeMapper;
import site.imcu.tape.pojo.Like;
import site.imcu.tape.service.ILikeService;
import site.imcu.tape.uitls.RedisUtil;

import java.util.Date;

/**
 * @author: MengHe
 * @date: 2020/3/17 16:14
 */
@Service
public class LikeServiceImpl implements ILikeService {
    @Autowired
    LikeMapper likeMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public Integer addLike(Like like) {
        if (!verifyLike(like)) {
            return 0;
        }
        like.setCreateMan(like.getUserId());
        like.setCreateTime(new Date());
        like.setDeleted(0);
        int result = likeMapper.insert(like);
        if (result!=1){
            return 0;
        }

        //clip likeCount++
        String likeCountKey = StrUtil.format("{}:{}:likedCount", like.getType(), like.getTargetId());
        if (redisUtil.hasKey(likeCountKey)){
            redisUtil.incrBy(likeCountKey,1);
        }else {
            redisUtil.append(likeCountKey, String.valueOf(1));
        }

        //redis存入user_like_clip 标志
        String likedKey = StrUtil.format("user:{}:like:{}:{}", like.getUserId(),like.getType(), like.getTargetId());
        redisUtil.append(likedKey,"true");

        return 1;
    }

    @Override
    public Integer unLike(Like like) {
        if (!verifyLike(like)) {
            return 0;
        }
        like.setUpdateTime(new Date());
        like.setUpdateMan(like.getUserId());
        int result = likeMapper.deleteLike(like);
        if (result!=1){
            return 0;
        }
        String likeCountKey = StrUtil.format("{}:{}:likedCount", like.getType(), like.getTargetId());
        redisUtil.incrBy(likeCountKey,-1);
        String likedKey = StrUtil.format("user:{}:like:{}:{}", like.getUserId(),like.getType(), like.getTargetId());
        redisUtil.delete(likedKey);
        return 1;
    }


    private boolean verifyLike(Like like){
        String commentType = "comment";
        String clipType = "clipType";
        return commentType.equals(like.getType()) || clipType.equals(like.getType());
    }
}
