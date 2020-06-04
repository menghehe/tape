package site.imcu.tape.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.imcu.tape.mapper.FriendMapper;
import site.imcu.tape.pojo.Friend;
import site.imcu.tape.pojo.User;
import site.imcu.tape.service.IFriendService;

import java.util.Date;

/**
* @author: MengHe
* @date: 2020/3/27 13:17
*/
@Service
public class FriendServiceImpl implements IFriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer addFriend(Friend friend) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<Friend>().eq("following", friend.getFollowing()).eq("follower", friend.getFollower());
        Friend existed = friendMapper.selectOne(queryWrapper);
        if (existed!=null){
            if (existed.getDeleted()==1){
                existed.setUpdateTime(new Date());
                existed.setUpdateMan(existed.getFollower());
                existed.setDeleted(0);
                return friendMapper.updateById(existed);
            }else {
                return 0;
            }
        }
        friend.setDeleted(0);
        friend.setCreateMan(friend.getFollower());
        friend.setCreateTime(new Date());
        return friendMapper.insert(friend);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer unFriend(Friend friend) {
        QueryWrapper<Friend> friendQueryWrapper = new QueryWrapper<Friend>().eq("following", friend.getFollowing()).eq("follower", friend.getFollower()).eq("is_deleted", 0);
        Friend existed = friendMapper.selectOne(friendQueryWrapper);
        //好友关系不存在，直接返回1
        if (existed==null){
            return 1;
        }
        existed.setDeleted(1);
        existed.setUpdateTime(new Date());
        existed.setUpdateMan(friend.getFollower());
        return friendMapper.updateById(existed);
    }

    @Override
    public Integer countFollower(Long userId) {
        return friendMapper.selectCount(new QueryWrapper<Friend>().eq("following",userId).eq("is_deleted",0));
    }

    @Override
    public Integer countFollowing(Long userId) {
        return friendMapper.selectCount(new QueryWrapper<Friend>().eq("follower",userId).eq("is_deleted",0));
    }


    @Override
    public IPage<User> getFollower(Page<Friend> page, Long userId) {
        IPage<User> followerPage = friendMapper.selectFollower(page, userId);
        for (User record : followerPage.getRecords()) {
            record.setFriendShipStatus(getFriendShipStatus(userId,record.getId()));
        }
        return followerPage;
    }

    @Override
    public IPage<User> getFollowing(Page<Friend> page, Long userId) {
        IPage<User> followingPage = friendMapper.selectFollowing(page, userId);
        for (User record : followingPage.getRecords()) {
            record.setFriendShipStatus(getFriendShipStatus(userId,record.getId()));
        }
        return followingPage;
    }

    @Override
    public Integer getFriendShipStatus(Long userId,Long friendId) {
        if (userId.equals(friendId)){
            return -1;
        }
        Integer following = friendMapper.selectCount(new QueryWrapper<Friend>().eq("follower", userId).eq("following", friendId).eq("is_deleted", 0));
        Integer follower = friendMapper.selectCount(new QueryWrapper<Friend>().eq("follower", friendId).eq("following", userId).eq("is_deleted", 0));
        if (following==0&&follower==0){
            return 0;
        }
        if (following==1&&follower==0){
            return 1;
        }
        if (follower==1&&following==0){
            return 2;
        }

        return 3;

    }

}
