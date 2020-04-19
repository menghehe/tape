package site.imcu.tape.security;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import site.imcu.tape.pojo.ResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mengh
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
      ResponseData responseData = ResponseData.builder().message("当前角色没有权限访问该权限").code(-11).build();
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(JSONObject.toJSONString(responseData));
   }
}
