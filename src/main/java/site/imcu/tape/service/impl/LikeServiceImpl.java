package site.imcu.tape.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.imcu.tape.mapper.LikeMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.Like;
import site.imcu.tape.service.ILikeService;
import site.imcu.tape.uitls.PushUtil;
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
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ClipServiceImpl clipService;
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    PushUtil pushUtil;

    Integer commentType = 2;
    Integer clipType = 1;
    @Override
    public Integer addLike(Like like) {
        if (verifyLike(like)) {
            return 0;
        }
        Like existed = likeMapper.selectOne(new QueryWrapper<Like>().eq("user_id", like.getUserId()).eq("type", like.getLikeType()).eq("target_id", like.getTargetId()));
        if (existed!=null){
            if (existed.getDeleted()==1){
                existed.setDeleted(0);
                existed.setUpdateMan(like.getUserId());
                existed.setUpdateTime(new Date());
                notifyLike(existed);
                return likeMapper.updateById(like);
            }else {
                return 0;
            }
        }
        like.setCreateMan(like.getUserId());
        like.setCreateTime(new Date());
        like.setDeleted(0);
        int result = likeMapper.insert(like);
        if (result!=1){
            return 0;
        }

        //clip likeCount++
        String likeCountKey = StrUtil.format("{}:{}:likedCount", like.getLikeType(), like.getTargetId());
        if (redisUtil.hasKey(likeCountKey)){
            redisUtil.incrBy(likeCountKey,1);
        }else {
            redisUtil.append(likeCountKey, String.valueOf(1));
        }

        //redis存入user_like_clip 标志
        String likedKey = StrUtil.format("user:{}:like:{}:{}", like.getUserId(),like.getLikeType(), like.getTargetId());
        redisUtil.append(likedKey,"true");

        notifyLike(like);

        return 1;
    }

    @Override
    public Integer unLike(Like like) {
        QueryWrapper<Like> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.eq("type",like.getLikeType()).eq("targetId",like.getTargetId()).eq("user_id",like.getUserId());
        Like existed = likeMapper.selectOne(likeQueryWrapper);
        if (existed==null){
            return 0;
        }
        like.setDeleted(1);
        like.setUpdateTime(new Date());
        like.setUpdateMan(like.getUserId());
        likeMapper.updateById(like);
        String likeCountKey = StrUtil.format("{}:{}:likedCount", like.getLikeType(), like.getTargetId());
        redisUtil.incrBy(likeCountKey,-1);
        String likedKey = StrUtil.format("user:{}:like:{}:{}", like.getUserId(),like.getLikeType(), like.getTargetId());
        redisUtil.delete(likedKey);
        return 1;
    }


    private boolean verifyLike(Like like){
        return !commentType.equals(like.getLikeType()) && !clipType.equals(like.getLikeType());
    }


    private void notifyLike(Like like){
        if (commentType.equals(like.getLikeType())){

            Comment commentById = commentService.getCommentById(like.getTargetId());

            if (like.getUserId().equals(commentById.getUserId())){
                return;
            }

            pushUtil.push("评论收到一条点赞",commentById.getUserId().toString());

        }
        if (clipType.equals(like.getLikeType())){
            Clip clipById = clipService.getClipById(like.getTargetId());

            if (clipById.getCreator().equals(like.getUserId())){
                return;
            }

            pushUtil.push("视频收到一条点赞",clipById.getCreator().toString());
        }
    }
}
