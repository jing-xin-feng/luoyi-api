package com.example.filter;

import com.example.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 从请求头中获取Token
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 提取Token（去掉"Bearer "前缀）
        final String jwtToken = authHeader.substring(7);

        try {
            // 3. 验证Token是否有效
            if (jwtToken != null && jwtUtils.validateToken(jwtToken)) {
                // 4. 从Token中获取用户名
                String username = jwtUtils.getUsernameFromToken(jwtToken);

                // 5. 检查用户是否已经认证
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 6. 从数据库加载用户详细信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 7. 创建Authentication对象
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // 8. 设置请求详情
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 9. 将Authentication对象存入SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("JWT Token验证失败: " + e.getMessage());
        }

        // 10. 继续过滤器链
        filterChain.doFilter(request, response);
    }
}