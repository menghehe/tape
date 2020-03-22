package site.imcu.tape.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    Integer addClip(Clip clip);

    /**
     * 查询推荐clip
     * @return clipList
     */
    List<Clip> getRecommendList();

    /**
     * 条件查询
     * @param clip c
     * @param currentUser u
     * @return p
     */
    IPage<Clip> getClipPage(Clip clip, User currentUser);
}
