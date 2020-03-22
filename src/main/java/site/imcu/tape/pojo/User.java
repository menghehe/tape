package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MengHe
 * @date 2019/12/27 10:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "tape_user")
public class User extends Base implements UserDetails {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Length(min=4, max=16,message = "用户名长度需要大于4，小于16")
    private String username;
    @Length(min=6, max=20,message = "密码长度需要大于6，小于20")
    private String password;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private String signature;
    @TableField(exist = false)
    private Set<Authority> authoritySet = new HashSet<>();
    @TableField(exist = false)
    private Set<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritySet;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
