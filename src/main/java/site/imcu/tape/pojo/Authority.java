package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author MengHe
 * @date 2019/12/27 14:26
 */
@Data
@TableName(value = "tape_authority")
public class Authority implements GrantedAuthority {
    private String authority;
    private Integer userId;
    private Integer authorityId;

    @Override
    public String getAuthority() {
        return authority;
    }
}
