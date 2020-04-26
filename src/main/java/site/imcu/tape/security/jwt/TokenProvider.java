package site.imcu.tape.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import site.imcu.tape.service.impl.UserServiceImpl;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author root
 */
@Component
@Slf4j
public class TokenProvider {

   private static final String AUTHORITIES_KEY = "auth";

   private final String base64Secret;
   private final long tokenValidityInMilliseconds;

   private static final String AUTHORIZATION_HEADER = "Authorization";

   @Autowired
   UserServiceImpl userService;


   public TokenProvider(
      @Value("${jwt.base64-secret}") String base64Secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
      this.base64Secret = base64Secret;
      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
   }

   public String createToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));
      long now = (new Date()).getTime();
      Date validity;
      validity = new Date(now + this.tokenValidityInMilliseconds);
      site.imcu.tape.pojo.User user = userService.getUserByName(authentication.getName());
      return JWT.create()
              .withClaim("userId", user.getId())
              .withClaim("username", user.getUsername())
              .withClaim(AUTHORITIES_KEY, authorities)
              .withExpiresAt(validity)
              .sign(Algorithm.HMAC256(base64Secret));
   }

   public Authentication getAuthentication(String token) {
      Map<String, Claim> claims = JWT.require(Algorithm.HMAC256(base64Secret)).build().verify(token).getClaims();
      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).asString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
      User principal = new User(claims.get("username").asString(), "", authorities);
      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   public site.imcu.tape.pojo.User getCurrentUser(){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Object credentials = authentication.getCredentials();
      Map<String, Claim> claims = JWT.require(Algorithm.HMAC256(base64Secret)).build().verify(credentials.toString()).getClaims();
      Long userId = claims.get("userId").asLong();
      String username = claims.get("username").asString();
      site.imcu.tape.pojo.User user = new site.imcu.tape.pojo.User();
      user.setId(userId);
      user.setUsername(username);
      return user;
   }


   public boolean validateToken(String authToken) {
      try {
         JWT.require(Algorithm.HMAC256(base64Secret)).build().verify(authToken);
         return true;
      } catch (JWTVerificationException | IllegalArgumentException e) {
         log.info(e.getMessage());
      }
      return false;
   }
}
