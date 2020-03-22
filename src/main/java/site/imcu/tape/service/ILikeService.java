package site.imcu.tape.service;

import site.imcu.tape.pojo.Like;

/**
 * @author: MengHe
 * @date: 2020/3/17 16:12
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
}
