package site.imcu.tape.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.CommentServiceImpl;
import site.imcu.tape.uitls.RedisUtil;

import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/3/16 17:19
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Value("${tape.avatarBaseUrl}")
    private String avatarBaseUrl;
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/add")
    public ResponseData addComment(@RequestBody Comment comment){
        Integer currentUserId = tokenProvider.getCurrentUser().getId();
        comment.setUserId(currentUserId);
        Integer result = commentService.addComment(comment);
        if (result==1){
            return ResponseData.builder().code(1).build();
        }else {
            return ResponseData.builder().code(-1).build();
        }
    }

    @GetMapping("/get")
    public ResponseData getComment(Comment comment){
        User currentUser = tokenProvider.getCurrentUser();
        IPage<Comment> page = commentService.getComment(comment, currentUser);
        return ResponseData.builder().code(1).data(page).build();
    }

//    private List<Comment> fillUrl(List<Comment> commentList){
//        for (Comment comment : commentList) {
//            User user = comment.getUser();
//            user.setAvatar(avatarBaseUrl+user.getAvatar());
//            comment.setUser(user);
//        }
//        return commentList;
//    }
}
