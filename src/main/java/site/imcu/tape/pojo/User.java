package site.imcu.tape.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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
    private Long id;

    @Length(min=4, max=16,message = "用户名长度需要大于4，小于16")
    private String username;
    @Length(min=6, max=20,message = "密码长度需要大于6，小于20")
    private String password;
    private String nickname;
    private String avatar;
    private String signature;
    @TableField(value = "is_locked")
    private Integer locked;
    @TableField(exist = false)
    private Set<Authority> authorities;

    @TableField(exist = false)
    private Set<String> roles;

    @TableField(exist = false)
    private Integer followingCount;
    @TableField(exist = false)
    private Integer followerCount;
    @TableField(exist = false)
    private Integer clipCount;
    /**
     * 0未关注，1已关注，2对方关注了我，3互相关注,-1是我自己
     */
    @TableField(exist = false)
    private Integer friendShipStatus;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return this.getLocked().equals(0);
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
