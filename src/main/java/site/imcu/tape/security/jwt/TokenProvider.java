package site.imcu.tape.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
import java.util.stream.Collectors;

/**
 * @author root
 */
@Component
@Slf4j
public class TokenProvider implements InitializingBean {

   private static final String AUTHORITIES_KEY = "auth";

   private final String base64Secret;
   private final long tokenValidityInMilliseconds;

   private Key key;

   private static String AUTHORIZATION_HEADER = "Authorization";

   @Autowired
   UserServiceImpl userService;


   public TokenProvider(
      @Value("${jwt.base64-secret}") String base64Secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
      this.base64Secret = base64Secret;
      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
   }

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

   public String createToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));

      long now = (new Date()).getTime();
      Date validity;
      validity = new Date(now + this.tokenValidityInMilliseconds);

      site.imcu.tape.pojo.User user = userService.getUserByName(authentication.getName());

      return Jwts.builder()
         .setSubject(authentication.getName())
         .claim("userId", user.getId())
         .claim("username", user.getUsername())
         .claim(AUTHORITIES_KEY, authorities)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }

   public Authentication getAuthentication(String token) {
      Claims claims = Jwts.parser()
         .setSigningKey(key)
         .parseClaimsJws(token)
         .getBody();

      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);

      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   public site.imcu.tape.pojo.User getCurrentUser(){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//      Object principal = authentication.getPrincipal();
//      site.imcu.tape.pojo.User user1 = new site.imcu.tape.pojo.User();
//      BeanUtils.copyProperties(principal,user1);
      Object credentials = authentication.getCredentials();
      Claims claims = Jwts.parser()
              .setSigningKey(key)
              .parseClaimsJws(credentials.toString())
              .getBody();
      Integer userId = claims.get("userId", Integer.class);
      String username = claims.get("username", String.class);
      site.imcu.tape.pojo.User user = new site.imcu.tape.pojo.User();
      user.setId(userId);
      user.setUsername(username);
      return user;
   }


   public boolean validateToken(String authToken) {
      try {
         Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         log.info("Invalid JWT signature.");
         log.trace("Invalid JWT signature trace: {}", e);
      } catch (ExpiredJwtException e) {
         log.info("Expired JWT token.");
         log.trace("Expired JWT token trace: {}", e);
      } catch (UnsupportedJwtException e) {
         log.info("Unsupported JWT token.");
         log.trace("Unsupported JWT token trace: {}", e);
      } catch (IllegalArgumentException e) {
         log.info("JWT token compact of handler are invalid.");
         log.trace("JWT token compact of handler are invalid trace: {}", e);
      }
      return false;
   }
}
