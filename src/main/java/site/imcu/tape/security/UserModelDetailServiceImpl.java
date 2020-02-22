package site.imcu.tape.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.impl.UserServiceImpl;

/**
 * @author MengHe
 * @date 2019/12/27 14:33
 */
@Service
public class UserModelDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getUserByName(s);
        if (user==null){
            throw new UsernameNotFoundException("用户"+s+"不存在");
        }
        return user;
    }
}
