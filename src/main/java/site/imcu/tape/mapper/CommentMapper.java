package site.imcu.tape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.imcu.tape.pojo.Comment;
import site.imcu.tape.pojo.User;

/**
 * @author: MengHe
 * @date: 2020/3/14 16:18
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询评论
     *
     * @param page    分页参数
     * @param comment 评论
     * @param user 当前user
     * @return commentList
     */
    IPage<Comment> selectCommentList(@Param("page") Page<Comment> page, @Param("comment") Comment comment, @Param("user") User user);
}
