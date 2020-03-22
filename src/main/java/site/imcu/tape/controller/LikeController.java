package site.imcu.tape.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import site.imcu.tape.pojo.Like;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.LikeServiceImpl;

/**
 * @author: MengHe
 * @date: 2020/3/17 18:26
 */
@RestController("/api/like")
public class LikeController {
    @Autowired
    LikeServiceImpl likeService;
    @Autowired
    TokenProvider tokenProvider;


    @PostMapping("/add")
    public ResponseData addLike(Like like){
        Integer currentUserId = tokenProvider.getCurrentUser().getId();
        like.setUserId(currentUserId);
        Integer result = likeService.addLike(like);
        if (result==1){
            return ResponseData.builder().code(1).build();
        }else {
            return ResponseData.builder().code(-1).build();
        }
    }

    @PutMapping("/cancel")
    public ResponseData cancelLike(Like like){
        Integer currentUserId = tokenProvider.getCurrentUser().getId();
        like.setUserId(currentUserId);
        Integer result = likeService.unLike(like);
        if (result==1){
            return ResponseData.builder().code(1).build();
        }else {
            return ResponseData.builder().code(-1).build();
        }
    }
}
