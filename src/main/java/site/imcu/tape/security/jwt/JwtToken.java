package site.imcu.tape.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: MengHe
 * @date: 2020/2/22 14:35
 */
@Data
@AllArgsConstructor
public class JwtToken {
    private String token;
}
