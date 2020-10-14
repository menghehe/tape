package site.imcu.tape.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.imcu.tape.mapper.ClipMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.IClipService;
import site.imcu.tape.uitls.RedisKey;
import site.imcu.tape.uitls.RedisUtil;
import site.imcu.tape.uitls.shell.LocalCommandExecutor;
import site.imcu.tape.uitls.shell.LocalCommandExecutorImpl;

import java.util.*;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:35
 */
@Service
@Slf4j
public class ClipServiceImpl implements IClipService {

    @Value("${tape.video-source-file-path}")
    private String videoSourceFilePath;

    @Value("${tape.hls-path}")
    private String hlsPath;

    @Value("${tape.cover-path}")
    private String coverPath;

    @Autowired
    RedisKey redisKey;

    @Autowired
    ClipMapper clipMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserServiceImpl userService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer createClip(Clip clip) {
        LocalCommandExecutor commandExecutor = new LocalCommandExecutorImpl();
        String sourceFile = videoSourceFilePath + clip.getSourceFile();
        String cover = coverPath + clip.getCoverPath();
        String hls = hlsPath + clip.getClipPath();
        String sliceCommand = StrUtil.format("ffmpeg -i {} -vcodec copy -acodec copy -hls_list_size 0 -vbsf h264_mp4toannexb {}", sourceFile, hls);
        commandExecutor.executeCommand(sliceCommand, 50000);
        if (StringUtils.isEmpty(clip.getCoverTime())) {
            clip.setCoverTime("00:00:01.000");
        }
        String coverCommand = StrUtil.format("ffmpeg -i {} -ss {} -vframes 1 {}", sourceFile, clip.getCoverTime(), cover);
        commandExecutor.executeCommand(coverCommand, 50000);
        return clipMapper.insert(clip);
    }

    @Override
    public IPage<Clip> getClipPage(Page<Clip> page, Clip clip, User currentUser) {
        IPage<Clip> clipPage = clipMapper.selectClipPage(page, clip);
        return fillClipPage(clipPage, currentUser);
    }

    @Override
    public List<Clip> hotClip(User currentUser) {
        Set<String> idSet = redisUtil.zReverseRange(redisKey.clipHeat(), 0, 10);
        Map<Long, Integer> orderMap = new HashMap<>();
        int order = 0;
        for (String s : idSet) {
            orderMap.put(Long.valueOf(s), order);
            order++;
        }
        List<Clip> clipList = clipMapper.selectBatchIds(orderMap.keySet());
        fillClipList(clipList, currentUser);
        clipList.sort((o1, o2) -> orderMap.get(o1.getId()) < orderMap.get(o2.getId()) ? -1 : 0);
        return clipList;
    }

    @Override
    public Integer countClip(Long userId) {
        return clipMapper.selectCount(new QueryWrapper<Clip>().eq("creator", userId).eq("is_deleted", 0));
    }


    private IPage<Clip> fillClipPage(IPage<Clip> clipPage, User user) {
        for (Clip clip : clipPage.getRecords()) {
            fillData(clip, user);
        }
        return clipPage;
    }

    private Clip fillClip(Clip clip, User user) {
        fillData(clip, user);
        return clip;
    }

    private void fillClipList(List<Clip> clipList, User currentUser) {
        for (Clip clip : clipList) {
            fillData(clip, currentUser);
        }
    }

    private void fillData(Clip clip, User user) {
        String likedKey = redisKey.userLikedClip(user.getId(), clip.getId());
        Boolean liked = redisUtil.hasKey(likedKey);
        clip.setLiked(liked);

        String likeCountKey = redisKey.clipLikedCount(clip.getId());
        String likeCount = redisUtil.get(likeCountKey);
        clip.setLikeCount(NumberUtil.parseInt(likeCount));

        String commentCountKey = redisKey.clipCommentCount(clip.getId());
        String commentCount = redisUtil.get(commentCountKey);
        clip.setCommentCount(NumberUtil.parseInt(commentCount));
    }

    @Override
    public Clip getClipById(Long id, User currentUser) {
        Clip clip = clipMapper.selectById(id);
        if (clip == null) {
            return null;
        }
        return fillClip(clip, currentUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer updateClip(Clip clip) {
        return clipMapper.updateById(clip);
    }
}
