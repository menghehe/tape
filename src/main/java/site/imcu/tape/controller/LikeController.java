package site.imcu.tape.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.imcu.tape.pojo.Like;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.LikeServiceImpl;
import site.imcu.tape.uitls.PushUtil;

/**
 * @author: MengHe
 * @date: 2020/3/17 18:26
 */
@RequestMapping("/api/like")
@RestController
public class LikeController {
    @Autowired
    LikeServiceImpl likeService;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    PushUtil pushUtil;


    @PostMapping("/create")
    public ResponseData createLike(@RequestBody Like like){
        Long currentUserId = tokenProvider.getCurrentUser().getId();
        like.setFromId(currentUserId);
        Integer result = likeService.addLike(like);
        if (result==1){
            return ResponseData.builder().code(1).build();
        }else {
            return ResponseData.builder().code(-1).build();
        }
    }

    @PostMapping("/destroy")
    public ResponseData destroyLike(@RequestBody Like like){
        Long currentUserId = tokenProvider.getCurrentUser().getId();
        like.setFromId(currentUserId);
        Integer result = likeService.unLike(like);
        if (result==1){
            return ResponseData.builder().code(1).build();
        }else {
            return ResponseData.builder().code(-1).build();
        }
    }


}
