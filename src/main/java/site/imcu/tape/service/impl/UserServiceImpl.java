package site.imcu.tape.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import site.imcu.tape.mapper.AuthorityMapper;
import site.imcu.tape.mapper.UserMapper;
import site.imcu.tape.pojo.Authority;
import site.imcu.tape.pojo.User;
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
    @Override
    public User getUserByName(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user==null){
            return null;
        }
        Set<Authority> authoritySet = authorityMapper.selectAuthorityByUserId(user.getId());
        user.setAuthoritySet(authoritySet);
        Set<String> roles = new HashSet<>();
        for (Authority authority : authoritySet) {
            if (authority.getAuthority().contains("ROLE")){
                roles.add(authority.getAuthority());
            }
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public Integer addUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        Date now = new Date();
        user.setCreateTime(now);
        user.setCreateMan(0);
        int result = userMapper.insert(user);
        if (result==1){
            Authority authority = new Authority();
            authority.setAuthorityId(1);
            authority.setUserId(user.getId());
            authorityMapper.insertUserAuthority(authority);
        }
        return result;
    }
}
