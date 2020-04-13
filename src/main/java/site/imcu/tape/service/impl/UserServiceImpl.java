package site.imcu.tape.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import site.imcu.tape.mapper.AuthorityMapper;
import site.imcu.tape.mapper.UserMapper;
import site.imcu.tape.pojo.Authority;
import site.imcu.tape.pojo.User;
import site.imcu.tape.security.jwt.TokenProvider;
import site.imcu.tape.service.IUserService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: MengHe
 * @date: 2020/2/20 18:48
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    AuthorityMapper authorityMapper;
    @Autowired
    FriendServiceImpl friendService;
    @Autowired
    ClipServiceImpl clipService;
    @Autowired
    TokenProvider tokenProvider;
    @Override
    public User getUserByName(String username) {
        User user = userMapper.selectByName(username);
        if (user==null){
            return null;
        }

        Set<String> roles = new HashSet<>();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().startsWith("ROLE")){
                roles.add(authority.getAuthority().substring(5));
            }
        }
        user.setRoles(roles);
        user.setFollowerCount(friendService.countFollower(user.getId()));
        user.setFollowingCount(friendService.countFollowing(user.getId()));
        user.setClipCount(clipService.countClip(user.getId()));
        User currentUser;
        try {
            currentUser = tokenProvider.getCurrentUser();
            if (!username.equals(currentUser.getUsername())){
                user.setFriendShipStatus(friendService.getFriendShipStatus(currentUser.getId(),user.getId()));
            }
            return user;
        } catch (Exception e){
            return user;
        }

    }


    @Override
    public Integer addUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        Date now = new Date();
        user.setCreateTime(now);
        user.setCreateMan((long) 0);
        user.setAvatar("default.jpg");
        int result = userMapper.insert(user);
        if (result==1){
            Authority authority = new Authority();
            authority.setAuthorityId((long) 1);
            authority.setUserId(user.getId());
            authorityMapper.insertUserAuthority(authority);
        }
        return result;
    }

    @Override
    public Integer updateUserById(User user) {
        user.setUpdateTime(new Date());
        return userMapper.updateById(user);
    }

    @Override
    public IPage<User> getUserPage(Page<User> page, User user) {
        IPage<User> list = userMapper.selectPage(page, user);
        return setUserFiled(list);
    }


    private IPage<User> setUserFiled(IPage<User> userPage){
        for (User user : userPage.getRecords()) {
            user.setFollowerCount(friendService.countFollower(user.getId()));
            user.setFollowingCount(friendService.countFollowing(user.getId()));
            user.setClipCount(clipService.countClip(user.getId()));
            Set<String> roles = new HashSet<>();
            for (GrantedAuthority authority : user.getAuthorities()) {
                if (authority.getAuthority().startsWith("ROLE")){
                    roles.add(authority.getAuthority().substring(5));
                }
            }
            user.setRoles(roles);
        }
        return userPage;
    }
}
