package site.imcu.tape.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.imcu.tape.mapper.CommentMapper;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.ICommentService;
import site.imcu.tape.uitls.PushUtil;
import site.imcu.tape.uitls.RedisKey;
import site.imcu.tape.uitls.RedisUtil;

import java.util.Date;
import java.util.List;

/**
 * @author : MengHe
 * @date : 2020/3/14 16:22
 */
@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    PushUtil pushUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedisKey redisKey;
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer addComment(Comment comment) {
        comment.setCreateMan(comment.getFromId());
        comment.setCreateTime(new Date());
        comment.setDeleted(0);
        int result = commentMapper.insert(comment);
        if (result!=1){
            return 0;
        }
        String key =redisKey.clipCommentCount(comment.getClipId());
        if (redisUtil.hasKey(key)) {
            redisUtil.incrBy(key,1);
        }else {
            redisUtil.append(key,String.valueOf(1));
        }

        Double score = redisUtil.zScore(redisKey.clipHeat(), comment.getClipId().toString());
        if (score==null){
            redisUtil.zAdd(redisKey.clipHeat(), comment.getClipId().toString(), 2);
        }else {
            redisUtil.zAdd(redisKey.clipHeat(), comment.getClipId().toString(),score+2);
        }
        notifyComment(comment);
        return 1;
    }

    private void notifyComment(Comment comment){
        if (comment.getFromId().equals(comment.getToId())){
            return;
        }
        pushUtil.push("收到一条评论",comment.getToId().toString());
    }

    @Override
    public IPage<Comment> getCommentPage(Page<Comment> page, Comment comment, User currentUser) {
        IPage<Comment> pageResult = commentMapper.selectCommentPage(page, comment);
        List<Comment> commentList = fillComment(pageResult.getRecords(), currentUser);
        pageResult.setRecords(commentList);
        return pageResult;
    }

    private List<Comment> fillComment(List<Comment> commentList,User user){
        for (Comment comment : commentList) {
            String likedKey = redisKey.userLikedComment(user.getId(), comment.getId());
            comment.setLiked(redisUtil.hasKey(likedKey));

            String likedCountKey = redisKey.commentLikedCount(comment.getId());
            String likedCount = redisUtil.get(likedCountKey);

            comment.setLikedCount(NumberUtil.parseInt(likedCount));
        }
        return commentList;
    }


    @Override
    public List<Comment> getAll() {
        return commentMapper.selectList(new QueryWrapper<Comment>().eq("is_deleted", 0));
    }
}
