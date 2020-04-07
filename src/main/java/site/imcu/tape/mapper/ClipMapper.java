package site.imcu.tape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import site.imcu.tape.pojo.Clip;

import java.util.List;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:32
 */
@Repository
public interface ClipMapper extends BaseMapper<Clip> {


    /***
     * 查询推荐的clip
     * @return clipList
     */
    List<Clip> selectRecommendList(@Param("page") Page<Clip> page);


    /**
     * 条件查询
     *
     * @param clip 条件
     * @param page 条件
     * @return page
     */
    IPage<Clip> selectClipPage(@Param("page") Page<Clip> page, @Param("clip") Clip clip);

    /**
     * 按id查询
     * @param id id
     * @return clip
     */
    Clip selectById(@Param("id") Long id);


}
