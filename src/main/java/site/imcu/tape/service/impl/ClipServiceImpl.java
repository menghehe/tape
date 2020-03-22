package site.imcu.tape.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.imcu.tape.mapper.ClipMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.IClipService;
import site.imcu.tape.uitls.RedisUtil;

import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:35
 */
@Service
@Slf4j
public class ClipServiceImpl implements IClipService {
    @Autowired
    ClipMapper clipMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public Integer addClip(Clip clip) {
        return clipMapper.insertClip(clip);
    }

    @Override
    public IPage<Clip> getClipPage(Clip clip, User currentUser) {
        Page<Clip> page = new Page<>();
        BeanUtils.copyProperties(clip,page);
        IPage<Clip> clipPage = clipMapper.selectClipPage(page, clip);
        List<Clip> clips = fillClip(clipPage.getRecords(), currentUser);
        clipPage.setRecords(clips);
        return clipPage;
    }

    @Override
    public List<Clip> getRecommendList() {
        User user = new User();
        user.setId(0);
        List<Clip> clipList = clipMapper.selectRecommendList();
        return fillClip(clipList, user);
    }

    private List<Clip> fillClip(List<Clip> clipList, User user){
        for (Clip clip : clipList) {
            String likedKey = StrUtil.format("user:{}:like:clip{}", user.getId(), clip.getId());
            Boolean liked = redisUtil.hasKey(likedKey);
            clip.setLiked(liked);

            String likeCountKey = StrUtil.format("clip:{}:likeCount", clip.getId());
            String likeCount = redisUtil.get(likeCountKey);
            clip.setLikeCount(NumberUtil.parseInt(likeCount));

            String commentCountKey = StrUtil.format("clip:{}:commentCount", clip.getId());
            String commentCount = redisUtil.get(commentCountKey);
            clip.setCommentCount(NumberUtil.parseInt(commentCount));

        }

        return clipList;
    }

}
