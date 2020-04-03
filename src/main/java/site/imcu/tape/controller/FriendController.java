package site.imcu.tape.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.imcu.tape.pojo.Friend;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.FriendServiceImpl;

/**
 * @author: MengHe
 * @date: 2020/3/28 16:08
 */
@RequestMapping("/api/friend")
@RestController
public class FriendController {
    @Autowired
    FriendServiceImpl friendService;
    @Autowired
    TokenProvider tokenProvider;

    @PostMapping("/create")
    public ResponseData addFollow(@RequestBody Friend friend){
        friend.setFollower(tokenProvider.getCurrentUser().getId());
        Integer result = friendService.addFriend(friend);
        if (result==1){
            return ResponseData.builder().code(1).build();
        }else {
            return ResponseData.builder().code(-1).build();
        }
    }

    @PostMapping("/destroy")
    public ResponseData destroyFollow(@RequestBody Friend friend){
        friend.setFollower(tokenProvider.getCurrentUser().getId());
        Integer result = friendService.unFriend(friend);
        if (result==1){
            return ResponseData.builder().code(1).build();
        }else {
            return ResponseData.builder().code(-1).build();
        }
    }

    @GetMapping("/follower")
    public ResponseData getFollower(){
        Page<Friend> page = new Page<>();
        Long id = tokenProvider.getCurrentUser().getId();
        IPage<User> follower = friendService.getFollower(page, id);
        return ResponseData.builder().code(1).data(follower).build();
    }

    @GetMapping("/following")
    public ResponseData getFollowing(){
        Page<Friend> page = new Page<>();
        Long id = tokenProvider.getCurrentUser().getId();
        IPage<User> following = friendService.getFollowing(page, id);
        return ResponseData.builder().code(1).data(following).build();
    }
}
