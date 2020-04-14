package site.imcu.tape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.imcu.tape.pojo.User;

import java.util.List;

/**
 * @author mengh
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询用户列表
     *
     * @param page 分页参数
     * @param user 条件参数
     * @return 用户列表
     */
    IPage<User> selectPage(@Param("page") Page<User> page, @Param("user") User user);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User selectByName(@Param("username") String username);

}
