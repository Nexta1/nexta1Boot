package com.nexta1.core.filter;


import com.nexta1.core.security.AuthenticationUtils;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import com.nexta1.core.web.service.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 * 继承OncePerRequestFilter类的话  可以确保只执行filter一次， 避免执行多次
 *
 * @author valarchie
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @NonNull
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {
        LoginUserDetail loginUserDetail = tokenService.getLoginUser(request);
        if (loginUserDetail != null && AuthenticationUtils.getAuthentication() == null) {
            tokenService.refreshToken(loginUserDetail);
            // 如果没有将当前登录用户放入到上下文中的话，会认定用户未授权，返回用户未登陆的错误
            putCurrentLoginUserIntoContext(request, loginUserDetail);

            log.debug("request process in jwt token filter. get login user id: {}", loginUserDetail.getUserId());
        }
        chain.doFilter(request, response);
    }


    private void putCurrentLoginUserIntoContext(HttpServletRequest request, LoginUserDetail loginUserDetail) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginUserDetail,
                null, loginUserDetail.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
