package site.imcu.tape.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.CommentServiceImpl;
import site.imcu.tape.uitls.RedisUtil;

/**
 * @author : MengHe
 * @date : 2020/3/16 17:19
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/create")
    public ResponseData addComment(@RequestBody Comment comment){
        Long currentUserId = tokenProvider.getCurrentUser().getId();
        comment.setFromId(currentUserId);
        Integer result = commentService.addComment(comment);
        return ResponseData.builder().code(result==1?1:-1).build();
    }

    @PostMapping("/list")
    public ResponseData listComment(@RequestBody Comment comment){
        User currentUser = tokenProvider.getCurrentUser();
        Page<Comment> commentPage = getPageParam(comment);
        IPage<Comment> page = commentService.getCommentPage(commentPage,comment,currentUser);
        return ResponseData.builder().code(1).data(page).build();
    }

    @PostMapping("/to_me")
    public ResponseData getToMeComment(@RequestBody Comment comment){
        User currentUser = tokenProvider.getCurrentUser();
        comment.setToId(currentUser.getId());
        IPage<Comment> page = commentService.getCommentPage(getPageParam(comment),comment,currentUser);
        return ResponseData.builder().code(1).data(page).build();
    }

    private Page<Comment> getPageParam(Comment comment){
        Page<Comment> commentPage = new Page<>();
        if (comment.getSize()==null||comment.getCurrent()==null){
            return commentPage;
        }
        BeanUtils.copyProperties(comment,commentPage);
        return commentPage;
    }

}
