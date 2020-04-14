package site.imcu.tape.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
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
        return ResponseData.builder().code(result==1?1:-1).build();
    }

    @PostMapping("/destroy")
    public ResponseData destroyLike(@RequestBody Like like){
        Long currentUserId = tokenProvider.getCurrentUser().getId();
        like.setFromId(currentUserId);
        Integer result = likeService.unLike(like);
        return ResponseData.builder().code(result==1?1:-1).build();

    }

    @PostMapping("/to_me")
    public ResponseData getToMe(@RequestBody Like like){
        Long id = tokenProvider.getCurrentUser().getId();
        like.setToId(id);
        Page<Like> pageParam = getPageParam(like);
        IPage<Like> page = likeService.getPage(pageParam, like);
        return ResponseData.builder().code(1).data(page).build();
    }

    private Page<Like> getPageParam(Like like){
        Page<Like> likePage = new Page<>();
        if (like.getSize()==null||like.getCurrent()==null){
            return likePage;
        }
        BeanUtils.copyProperties(like, likePage);
        return likePage;
    }


}
