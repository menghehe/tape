package site.imcu.tape.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.JwtToken;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.UserServiceImpl;
import site.imcu.tape.uitls.PushUtil;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

/**
 * @author: MengHe
 * @date: 2020/2/21 10:56
 */
@CrossOrigin
@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Value("${tape.avatarPath}")
    private String avatarPath;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    PushUtil pushUtil;

    @PostMapping("/register")
    public ResponseData register(@Valid @RequestBody User user){
        User userByName = userService.getUserByName(user.getUsername());
        if (userByName!=null){
            return ResponseData.builder().code(-1).message("用户名已存在").build();
        }
        Integer result = userService.createUser(user);
        return  ResponseData.builder().code(result==1?1:-1).build();
    }

    @PostMapping("/login")
    public ResponseData login(@Valid @RequestBody User user){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        return ResponseData.builder().data(jwtToken).code(1).message("登录成功").build();
    }

    @GetMapping("/info")
    public ResponseData getInfo(String username){
        String currentUser = tokenProvider.getCurrentUser().getUsername();
        if (StrUtil.isBlank(username)){
            username = currentUser;
        }
        User user = userService.getUserByName(username);
        return ResponseData.builder().code(1).data(user).build();
    }


    @PostMapping("/logout")
    public ResponseData logout(){
        return ResponseData.builder().code(1).build();
    }

    @PostMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData getList(@RequestBody User user){
        IPage<User> page = userService.getUserPage(getPageParam(user), user);
        return ResponseData.builder().code(1).data(page).build();
    }

    @PostMapping("/avatar")
    public ResponseData changeAvatar(MultipartFile avatar) throws IOException {
        String spot = ".";
        String originalFilename = avatar.getOriginalFilename();
        String fileType;
        if(!StringUtils.isEmpty(originalFilename)&&originalFilename.contains(spot)){
            fileType = originalFilename.substring(originalFilename.lastIndexOf(spot));
        }else{
            return ResponseData.builder().code(-1).build();
        }
        String uuid = IdUtil.fastSimpleUUID();
        String fileName = uuid + fileType;
        File targetFile = new File(avatarPath,fileName);
        avatar.transferTo(targetFile);
        Long userId = tokenProvider.getCurrentUser().getId();
        User user = new User();
        user.setAvatar(fileName);
        user.setId(userId);
        user.setUpdateMan(userId);
        Integer result = userService.updateUserById(user);
        return ResponseData.builder().code(result==1?1:-1).build();
    }

    @PostMapping("/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData lock(@RequestBody User user){
        User userById = userService.getUserById(user.getId());
        for (GrantedAuthority authority : userById.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())){
                return ResponseData.builder().code(-1).message("无法封禁管理员").build();
            }
        }
        user.setLocked(1);
        user.setUpdateMan(tokenProvider.getCurrentUser().getId());
        Integer result = userService.updateUserById(user);
        return ResponseData.builder().code(result.equals(1)?1:-1).build();
    }
    @PostMapping("/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData unlock(@RequestBody User user){
        user.setLocked(0);
        user.setUpdateMan(tokenProvider.getCurrentUser().getId());
        Integer result = userService.updateUserById(user);
        return ResponseData.builder().code(result.equals(1)?1:-1).build();
    }

    private Page<User> getPageParam(User user){
        Page<User> pageParam = new Page<>();
        if (user.getCurrent()==null||user.getSize()==null){
            return pageParam;
        }
        BeanUtils.copyProperties(user,pageParam);
        return pageParam;
    }




}
