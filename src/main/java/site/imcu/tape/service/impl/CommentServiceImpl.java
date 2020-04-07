package site.imcu.tape.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.imcu.tape.mapper.CommentMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.ICommentService;
import site.imcu.tape.uitls.PushUtil;
import site.imcu.tape.uitls.RedisUtil;

import java.util.Date;
import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/3/14 16:22
 */
@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ClipServiceImpl clipService;
    @Autowired
    PushUtil pushUtil;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public Integer addComment(Comment comment) {
        comment.setCreateMan(comment.getFromId());
        comment.setCreateTime(new Date());
        comment.setDeleted(0);
        int result = commentMapper.insert(comment);
        if (result!=1){
            return 0;
        }
        String key = StrUtil.format("clip:{}:commentCount", comment.getClipId());
        if (redisUtil.hasKey(key)) {
            redisUtil.incrBy(key,1);
        }else {
            redisUtil.append(key,String.valueOf(1));
        }
        if (!comment.getToId().equals(comment.getFromId())){
            pushUtil.push("收到一条评论",comment.getToId().toString());
        }
        return 1;
    }

    @Override
    public IPage<Comment> getComment(Comment comment, User user) {
        Page<Comment> commentPage = new Page<>();
        BeanUtils.copyProperties(comment,commentPage);
        IPage<Comment> pageResult = commentMapper.selectCommentPage(commentPage, comment);
        List<Comment> commentList = fillComment(pageResult.getRecords(), user);
        pageResult.setRecords(commentList);
        return pageResult;
    }

    private List<Comment> fillComment(List<Comment> commentList,User user){
        for (Comment comment : commentList) {
            String likedKey = StrUtil.format("user:{}:like:comment:{}", user.getId(), comment.getId());
            comment.setLiked(redisUtil.hasKey(likedKey));

            String likedCountKey = StrUtil.format("comment:{}:likedCount", comment.getId());
            String likedCount = redisUtil.get(likedCountKey);

            comment.setLikedCount(NumberUtil.parseInt(likedCount));
        }
        return commentList;
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentMapper.selectById(id);
    }

}
