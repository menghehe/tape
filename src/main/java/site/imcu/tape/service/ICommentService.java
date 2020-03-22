package site.imcu.tape.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.User;

import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/3/14 16:19
 */
public interface ICommentService {

    /**
     * 新建评论
     * @param comment 评论
     * @return 结果
     */
    Integer addComment(Comment comment);

    /**
     * 查询clip的评论
     * @param comment clipId
     * @param user user
     * @return 评论
     */
    IPage<Comment> getComment(Comment comment, User user);

}
