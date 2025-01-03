package org.example.config;


import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.example.config.JwtUtil.signature;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        // 如果找不到 token，则返回未经授权的错误响应
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString("No Login"));
            return false;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(signature)
                    .parseClaimsJws(token)
                    .getBody();  //获取claims内容,包含了用户名等信息
            // 获取用户名
            String username = (String) claims.get("username");
            Date exp = claims.getExpiration();// 获取token过期时间
            Date now = new Date();
            // 判断是否过期
            if (now.after(exp)) {
                // token已过期,返回错误响应或者抛出异常
                throw new ExpiredJwtException(null, null, "Token expired");
            }
        } catch (ExpiredJwtException e) {
            // 处理过期的令牌
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString("Token is expired"));
            return false;
        } catch (Exception e) {
            // 其他解析失败的情况
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString("Token is invalid"));
            return false;
        }
        return true;
    }
}
