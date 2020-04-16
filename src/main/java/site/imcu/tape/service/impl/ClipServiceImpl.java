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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    @Autowired
    UserServiceImpl userService;
    @Override
    public Integer createClip(Clip clip) {
        LocalCommandExecutor commandExecutor = new LocalCommandExecutorImpl();
        String sourceFile = sourceFilePath + clip.getSourceFile();
        String cover = coverPath + clip.getCoverPath();
        String hls = hlsPath + clip.getClipPath();

        String sliceCommand = StrUtil.format("ffmpeg -i {} -vcodec copy -acodec copy -hls_list_size 0 -vbsf h264_mp4toannexb {}", sourceFile, hls);
        log.info("执行前时间{}",new Date());
        commandExecutor.executeCommand(sliceCommand, 50000);
        log.info("执行前时间{}",new Date());
        if (StringUtils.isEmpty(clip.getCoverTime())){
            clip.setCoverTime("00:00:01.000");
        }
        String coverCommand = StrUtil.format("ffmpeg -i {} -ss {} -vframes 1 {}", sourceFile,clip.getCoverTime(), cover);
        commandExecutor.executeCommand(coverCommand, 50000);
        log.info("执行前时间{}",new Date());

        return clipMapper.insert(clip);
    }

    @Override
    public IPage<Clip> getClipPage(Page<Clip> page, Clip clip, User currentUser) {
        IPage<Clip> clipPage = clipMapper.selectClipPage(page, clip);
        return fillClipPage(clipPage,currentUser);
    }

    @Override
    public List<Clip> hotClip(User currentUser) {
        Set<String> stringSet = redisUtil.zReverseRange(redisKey.clipHeat(), 0, 10);
        List<Long> idList = new ArrayList<>();
        for (String s : stringSet) {
            idList.add(Long.valueOf(s));
        }
        List<Clip> clipList = clipMapper.selectBatchIds(idList);
        fillClipList(clipList, currentUser);
        List<Clip> resultList = new ArrayList<>();
        for (Long aLong : idList) {
            for (Clip clip : clipList) {
                if (clip.getId().equals(aLong)){
                    User userById = userService.getUserById(clip.getCreator());
                    clip.setUser(userById);
                    resultList.add(clip);
                }
            }
        }
        return resultList;
    }

    @Override
    public Integer countClip(Long userId) {
        return clipMapper.selectCount(new QueryWrapper<Clip>().eq("creator",userId).eq("is_deleted",0));
    }


    private IPage<Clip> fillClipPage(IPage<Clip> clipPage, User user){
        for (Clip clip : clipPage.getRecords()) {
            fillData(clip, user);
        }
        return clipPage;
    }

    private Clip fillClip(Clip clip, User user){
        fillData(clip, user);
        return clip;
    }

    private void fillClipList(List<Clip> clipList,User currentUser){
        for (Clip clip : clipList) {
            fillData(clip,currentUser);
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
        if (clip==null){
            return null;
        }
        return fillClip(clip,currentUser);
    }

    @Override
    public Integer updateClip(Clip clip) {
        return clipMapper.updateById(clip);
    }
}
