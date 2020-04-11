package site.imcu.tape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.imcu.tape.pojo.Like;

/**
 * @author: MengHe
 * @date: 2020/3/14 16:18
 */
@Repository
public interface LikeMapper extends BaseMapper<Like> {
    IPage<Like> selectByPage(@Param("page") Page<Like> page, @Param("like") Like like);
}
