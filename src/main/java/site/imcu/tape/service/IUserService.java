package site.imcu.tape.service;

import site.imcu.tape.pojo.User;

/**
 * @author: MengHe
 * @date: 2020/2/20 15:58
 */
public interface IUserService {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User getUserByName(String username);

    /**添加新用户
     * @param user 用户
     * @return 结果
     */
    Integer addUser(User user);
}
