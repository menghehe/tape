package site.imcu.tape.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import site.imcu.tape.pojo.ResponseData;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.JwtToken;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.impl.UserServiceImpl;

import javax.validation.Valid;

/**
 * @author: MengHe
 * @date: 2020/2/21 10:56
 */
@CrossOrigin
@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Value("${tape.avatarBaseUrl}")
    private String avatarBaseUrl;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseData register(@Valid @RequestBody User user){
        User userByName = userService.getUserByName(user.getUsername());
        if (userByName!=null){
            return ResponseData.builder().code(-1).message("用户名已存在").build();
        }
        Integer result = userService.addUser(user);

        if (result==1){
            return ResponseData.builder().code(result).message("注册成功").build();
        }else {
            return ResponseData.builder().code(result).message("注册失败，未知错误").build();
        }
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
    public ResponseData getInfo(){
        String username = tokenProvider.getCurrentUser().getUsername();
        User user = userService.getUserByName(username);
        return ResponseData.builder().code(1).data(user).build();
    }

    @PostMapping("/logout")
    public ResponseData logout(){
        return ResponseData.builder().code(1).build();
    }

//    private User fillUrl(User user){
//        user.setAvatar(avatarBaseUrl+user.getAvatar());
//        return user;
//    }
}
