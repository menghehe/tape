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
import org.springframework.util.StringUtils;
import site.imcu.tape.mapper.ClipMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.IClipService;
import site.imcu.tape.uitls.RedisKey;
import site.imcu.tape.uitls.RedisUtil;
import site.imcu.tape.uitls.shell.LocalCommandExecutor;
import site.imcu.tape.uitls.shell.LocalCommandExecutorImpl;

import java.util.Collections;
import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:35
 */
@Service
@Slf4j
public class ClipServiceImpl implements IClipService {

    @Value("${tape.sourceFilePath}")
    private String sourceFilePath;

    @Value("${tape.hlsPath}")
    private String hlsPath;

    @Value("${tape.coverPath}")
    private String coverPath;

    @Autowired
    RedisKey redisKey;

    @Autowired
    ClipMapper clipMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public Integer createClip(Clip clip) {
        LocalCommandExecutor commandExecutor = new LocalCommandExecutorImpl();
        String sourceFile = sourceFilePath + clip.getSourceFile();
        String cover = coverPath + clip.getCoverPath();
        String hls = hlsPath + clip.getClipPath();

        String sliceCommand = StrUtil.format("ffmpeg -i {} -vcodec copy -acodec copy -hls_list_size 0 -vbsf h264_mp4toannexb {}", sourceFile, hls);
        commandExecutor.executeCommand(sliceCommand, 5000);
        if (StringUtils.isEmpty(clip.getCoverTime())){
            clip.setCoverTime("00:00:01.000");
        }
        String coverCommand = StrUtil.format("ffmpeg -i {} -ss {} -vframes 1 {}", sourceFile,clip.getCoverTime(), cover);
        commandExecutor.executeCommand(coverCommand, 5000);

        return clipMapper.insert(clip);
    }

    @Override
    public IPage<Clip> getClipPage(Page<Clip> page, Clip clip, User currentUser) {
        IPage<Clip> clipPage = clipMapper.selectClipPage(page, clip);
        List<Clip> clips = fillClip(clipPage.getRecords(), currentUser);
        clipPage.setRecords(clips);
        return clipPage;
    }

    @Override
    public Integer countClip(Long userId) {
        return clipMapper.selectCount(new QueryWrapper<Clip>().eq("creator",userId).eq("is_deleted",0));
    }

    @Override
    public List<Clip> getRecommendList(Page<Clip> page, User currentUser) {
        List<Clip> clipList = clipMapper.selectRecommendList(page);
        return fillClip(clipList, currentUser);
    }

    private List<Clip> fillClip(List<Clip> clipList, User user){
        if (user==null){
            user = new User();
            user.setId((long) 0);
        }
        for (Clip clip : clipList) {
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
        return clipList;
    }

    @Override
    public Clip getClipById(Long id, User user) {
        Clip clip = clipMapper.selectById(id);
        if (clip==null){
            return null;
        }
        List<Clip> clipList = fillClip(Collections.singletonList(clip), user);
        return clipList.get(0);
    }

    @Override
    public Integer updateClip(Clip clip) {
        return clipMapper.updateById(clip);
    }
}
