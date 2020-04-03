package site.imcu.tape.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.imcu.tape.pojo.Friend;
import site.imcu.tape.pojo.User;

/**
* @author: MengHe
* @date: 2020/3/27 13:17
*/
public interface IFriendService {
    /**
     * 添加好友关系
     * @param friend 关系
     * @return 结果
     */
    Integer addFriend(Friend friend);

    /**
     * 取消好友关系
     * @param friend 关系
     * @return 结果
     */
    Integer unFriend(Friend friend);


    /**
     * 查询用户粉丝人数
     * @param userId 用户id
     * @return 粉丝人数
     */
    Integer countFollower(Long userId);

    /**
     * 查询用户关注人数
     * @param userId 用户id
     * @return 关注人数
     */
    Integer countFollowing(Long userId);


    /**查询好友关系
     * @param userId uid
     * @param friendId fid
     * @return 关系
     */
    Integer getFriendShipStatus(Long userId,Long friendId);

    /**
     * 查询关注的人
     * @param page
     * @param userId
     * @return
     */
    IPage<User> getFollower(Page<Friend> page, Long userId);

    /**
     * 查询粉丝
     * @param page
     * @param userId
     * @return
     */
    IPage<User> getFollowing(Page<Friend> page, Long userId);


}
