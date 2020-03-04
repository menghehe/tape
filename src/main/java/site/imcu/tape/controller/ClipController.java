package site.imcu.tape.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.ClipServiceImpl;
import site.imcu.tape.uitls.shell.ExecuteResult;
import site.imcu.tape.uitls.shell.LocalCommandExecutor;
import site.imcu.tape.uitls.shell.LocalCommandExecutorImpl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:35
 */
@RestController
@RequestMapping("/api/clip")
@Slf4j
public class ClipController {

    @Value("${tape.path}")
    private String tapePath;

    @Autowired
    ClipServiceImpl clipService;
    @Autowired
    TokenProvider tokenProvider;

    @PostMapping("/new")
    public ResponseData newClip(@RequestParam("video") MultipartFile video, String title) throws IOException {
        Clip clip = new Clip();
        clip.setTitle(title);
        Integer userId = tokenProvider.getCurrentUserId();
        User user = new User();
        user.setId(userId);
        clip.setUser(user);
        String originalFilename = video.getOriginalFilename();
        String fileType= null;
        assert originalFilename != null;
        if(originalFilename.contains(".")){
            fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        }else{
            fileType = "";
        }

        String uuid = IdUtil.fastSimpleUUID();
        String fileName = uuid + fileType;
        File targetFile = new File(tapePath+"/sourceFile/",fileName);
        video.transferTo(targetFile);

        LocalCommandExecutor commandExecutor = new LocalCommandExecutorImpl();
        String source = targetFile.getAbsolutePath();

        String out = tapePath+"/hls/"+uuid+".m3u8";
        String command = "ffmpeg -i "+ source + " -vcodec copy -acodec copy -hls_list_size 0 -vbsf h264_mp4toannexb " +out;
        ExecuteResult executeResult = commandExecutor.executeCommand(command, 5000);

        clip.setSourceFile(fileName);
        clip.setClipPath(uuid+".m3u8");

        String thumbOut = tapePath+"/cover/"+uuid+".png";

        String thumbCommand = "ffmpeg -i "+source+" -ss 00:00:01.000 -vframes 1 " +thumbOut;

        commandExecutor.executeCommand(thumbCommand, 5000);

        clip.setCoverPath(uuid+".png");

        Date now = new Date();
        clip.setCreateMan(userId);
        clip.setCreateTime(now);
        Integer result = clipService.addClip(clip);
        return ResponseData.builder().code(result).build();
    }
}
