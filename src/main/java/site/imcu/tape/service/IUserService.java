package site.imcu.tape.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 更新用户信息
     * @param user user
     * @return 1 成功 0失败
     */
    Integer updateUserById(User user);

    /**
     * 按条件查找用户
     * @param page 分页参数
     * @param user 条件
     * @return 符合user的user
     */
    IPage<User> getUserList(Page<User> page, User user);
}
