package site.imcu.tape.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.imcu.tape.pojo.Like;

import java.util.List;

/**
 * @author : MengHe
 * @date : 2020/3/17 16:12
 */
public interface ILikeService {

    /**
     * 添加点赞
     * @param like like
     * @return 1 0
     */
    Integer addLike(Like like);

    /**
     * 取消喜欢
     * @param like like
     * @return 0 1
     */
    Integer unLike(Like like);


    /**
     * 分页查询
     * @param page 分页参数
     * @param like like
     * @return page
     */
    IPage<Like> getPage(Page<Like> page,Like like);


    /**
     * 查询全部,仅redis同步使用
     * @return likeList
     */
    List<Like> getAll();
}
