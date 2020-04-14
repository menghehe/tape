package site.imcu.tape.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.imcu.tape.pojo.Friend;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.FriendServiceImpl;

/**
 * @author : MengHe
 * @date : 2020/3/28 16:08
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

    @PostMapping("/follower")
    public ResponseData getFollower(@RequestBody Friend friend){
        Page<Friend> pageParam = getPageParam(friend);
        Long id = tokenProvider.getCurrentUser().getId();
        IPage<User> follower = friendService.getFollower(pageParam, id);
        return ResponseData.builder().code(1).data(follower).build();
    }

    @PostMapping("/following")
    public ResponseData getFollowing(@RequestBody Friend friend){
        Page<Friend> pageParam = getPageParam(friend);
        Long id = tokenProvider.getCurrentUser().getId();
        IPage<User> following = friendService.getFollowing(pageParam, id);
        return ResponseData.builder().code(1).data(following).build();
    }

    private Page<Friend> getPageParam(Friend user){
        Page<Friend> page = new Page<>();
        if (user.getSize()==null||user.getCurrent()==null){
            return page;
        }
        BeanUtils.copyProperties(user, page);
        return page;
    }
}
