package site.imcu.tape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import site.imcu.tape.pojo.Friend;
import site.imcu.tape.pojo.User;

/**
 * @author: MengHe
 * @date: 2020/3/27 13:17
 */
public interface FriendMapper extends BaseMapper<Friend> {

    /**
     * 查询用户的粉丝
     * @param page 分页参数
     * @param userId 用户Id
     * @return 粉丝列表
     */
    IPage<User> selectFollower(@Param("page") Page<Friend> page, @Param("userId") Long userId);

    /**
     * 查询用户的关注
     * @param page 分页参数
     * @param userId 用户Id
     * @return 关注列表
     */
    IPage<User> selectFollowing(@Param("page") Page<Friend> page, @Param("userId") Long userId);
}