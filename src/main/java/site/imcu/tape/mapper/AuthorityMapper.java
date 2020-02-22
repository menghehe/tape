package site.imcu.tape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import site.imcu.tape.pojo.Authority;

import java.util.Set;

/**
 * @author mengh
 */
@Repository
public interface AuthorityMapper extends BaseMapper<Authority> {
    /**
     * 查询用户权限
     * @param userId 用户id
     * @return 权限集合
     */
    Set<Authority> selectAuthorityByUserId(Integer userId);

    /**
     * 为用户添加权限
     * @param authority 权限
     * @return 结果
     */
    int insertUserAuthority(Authority authority);
}
