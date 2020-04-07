package site.imcu.tape.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.imcu.tape.pojo.Clip;
import site.imcu.tape.pojo.User;

import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:33
 */
public interface IClipService {

    /**
     * 添加新的短视频
     * @param clip clip
     * @return result
     */
    Integer createClip(Clip clip);

    /**
     * 查询推荐clip
     * @param page page
     * @param currentUser
     * @return clipList
     */
    List<Clip> getRecommendList(Page<Clip> page, User currentUser);

    /**
     * 条件查询
     *
     * @param page
     * @param clip c
     * @param currentUser u
     * @return p
     */
    IPage<Clip> getClipPage(Page<Clip> page, Clip clip, User currentUser);

    /**
     * 查询用户clip数
     * @param userId userId
     * @return clip 数
     */
    Integer countClip(Long userId);

    /**
     * 根据id获取clip
     * @param id id
     * @param user
     * @return clip
     */
    Clip getClipById(Long id, User user);

    /**
     * 更新clip
     * @param clip clip
     * @return 1成功 2失败
     */
    Integer updateClip(Clip clip);

}
