package site.imcu.tape.mapper;

import org.springframework.stereotype.Repository;
import site.imcu.tape.pojo.Clip;

/**
 * @author: MengHe
 * @date: 2020/2/23 15:32
 */
@Repository
public interface ClipMapper {

    /**add new clip
     * @param clip clip
     * @return result
     */
    Integer insertClip(Clip clip);
}
