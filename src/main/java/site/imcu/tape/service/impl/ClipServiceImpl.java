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
import site.imcu.tape.mapper.ClipMapper;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.IClipService;
import site.imcu.tape.uitls.RedisUtil;
import site.imcu.tape.uitls.shell.LocalCommandExecutor;
import site.imcu.tape.uitls.shell.LocalCommandExecutorImpl;

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

        String coverCommand = StrUtil.format("ffmpeg -i {} -ss 00:00:01.000 -vframes 1 {}", sourceFile, cover);
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
    public List<Clip> getRecommendList(Page<Clip> page) {
        List<Clip> clipList = clipMapper.selectRecommendList(page);
        return fillClip(clipList, null);
    }

    private List<Clip> fillClip(List<Clip> clipList, User user){
        if (user==null){
            user = new User();
            user.setId((long) 0);
        }
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

    @Override
    public Clip getClipById(Long id) {
        return clipMapper.selectById(id);
    }

    @Override
    public Integer updateClip(Clip clip) {
        return clipMapper.updateById(clip);
    }
}
