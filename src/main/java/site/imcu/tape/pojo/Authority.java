package site.imcu.tape.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author MengHe
 * @date 2019/12/27 14:26
 */
@Data
public class Authority implements GrantedAuthority {
    private String authority;
    private Integer userId;
    private Integer authorityId;

    @Override
    public String getAuthority() {
        return authority;
    }
}
