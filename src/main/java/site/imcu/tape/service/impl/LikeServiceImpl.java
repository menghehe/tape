package site.imcu.tape.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.imcu.tape.mapper.LikeMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.Like;
import site.imcu.tape.service.ILikeService;
import site.imcu.tape.uitls.PushUtil;
import site.imcu.tape.uitls.RedisKey;
import site.imcu.tape.uitls.RedisUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    RedisKey redisKey;
    @Autowired
    PushUtil pushUtil;
    Integer commentType = 2;
    Integer clipType = 1;
    Map<Integer,String> typeMap = new HashMap<>(2);

    {
        typeMap.put(clipType,"clip");
        typeMap.put(commentType,"comment");
    }
    @Override
    public Integer addLike(Like like) {
        if (verifyLike(like)) {
            return 0;
        }
        Like existed = likeMapper.selectOne(new QueryWrapper<Like>().eq("from_id", like.getFromId()).eq("like_type", like.getLikeType()).eq("target_id", like.getTargetId()));
        if (existed!=null){
            if (existed.getDeleted()==1){
                existed.setDeleted(0);
                existed.setUpdateMan(like.getFromId());
                existed.setUpdateTime(new Date());
                int result = likeMapper.updateById(existed);
                if (result==1){
                    likeMark(existed);
                    notifyLike(like);
                }
                return result;
            }else {
                return 0;
            }
        }
        like.setCreateMan(like.getFromId());
        like.setCreateTime(new Date());
        like.setDeleted(0);
        int result = likeMapper.insert(like);
        if (result!=1){
            return 0;
        }

        likeMark(like);
        notifyLike(like);

        return 1;
    }

    @Override
    public Integer unLike(Like like) {
        Like existed = likeMapper.selectOne(new QueryWrapper<Like>().eq("from_id", like.getFromId()).eq("like_type", like.getLikeType()).eq("target_id", like.getTargetId()));
        if (existed==null){
            return 0;
        }
        existed.setDeleted(1);
        existed.setUpdateTime(new Date());
        existed.setUpdateMan(like.getFromId());
        int result = likeMapper.updateById(existed);
        if (result==1){
            String likeCountKey = existed.getLikeType()==1?redisKey.clipLikedCount(existed.getTargetId()):redisKey.commentLikedCount(existed.getTargetId());
            redisUtil.incrBy(likeCountKey,-1);
            String likedKey = existed.getLikeType()==1?redisKey.userLikedClip(existed.getFromId(),existed.getTargetId()):redisKey.userLikedComment(existed.getFromId(),existed.getTargetId());
            redisUtil.delete(likedKey);
        }
        return result;
    }

    @Override
    public IPage<Like> getPage(Page<Like> page, Like like) {
        return likeMapper.selectByPage(page, like);
    }

    private boolean verifyLike(Like like){
        return !commentType.equals(like.getLikeType()) && !clipType.equals(like.getLikeType());
    }

    private void notifyLike(Like like){
        pushUtil.push("收到一条点赞",like.getToId().toString());
    }

    private void likeMark(Like like){
        //clip likeCount++
        String likeCountKey = like.getLikeType()==1?redisKey.clipLikedCount(like.getTargetId()):
                redisKey.commentLikedCount(like.getTargetId());
        if (redisUtil.hasKey(likeCountKey)){
            redisUtil.incrBy(likeCountKey,1);
        }else {
            redisUtil.append(likeCountKey, String.valueOf(1));
        }

        if (like.getLikeType()==1){
            Double score = redisUtil.zScore(redisKey.clipHeat(), like.getTargetId().toString());
            if (score==null){
                redisUtil.zAdd(redisKey.clipHeat(), like.getTargetId().toString(), 1);
            }else {
                redisUtil.zAdd(redisKey.clipHeat(), like.getTargetId().toString(),score+1 );
            }
        }

        //redis存入user_like_clip 标志
        String likedKey = like.getLikeType()==1?redisKey.userLikedClip(like.getFromId(),like.getTargetId()):
                redisKey.userLikedComment(like.getFromId(),like.getTargetId());
        redisUtil.append(likedKey,"true");
    }

    @Override
    public List<Like> getAll() {
        return likeMapper.selectList(new QueryWrapper<Like>().eq("is_deleted", 0));
    }
}
