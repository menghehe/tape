package site.imcu.tape.security;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import site.imcu.tape.pojo.ResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
      ResponseData responseData = ResponseData.builder().message(authException.getMessage()).code(-2).build();
      response.getWriter().write(JSONObject.toJSONString(responseData));
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(JSONObject.toJSONString(responseData));
   }


}
