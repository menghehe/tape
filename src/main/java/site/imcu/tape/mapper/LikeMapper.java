package site.imcu.tape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.imcu.tape.pojo.Like;

/**
 * @author: MengHe
 * @date: 2020/3/14 16:18
 */
@Repository
public interface LikeMapper extends BaseMapper<Like> {

    /**
     * 喜欢设置删除标志
     * @param like
     * @return
     */
    int deleteLike(@Param("like") Like like);
}
