package com.nexta1.core.web.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.nexta1.common.constant.Constants;
import com.nexta1.common.constant.Constants.Token;
import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import com.nexta1.core.cache.redis.RedisCacheService;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author valarchie
 */
@Component
@Slf4j
@Data
@RequiredArgsConstructor
public class TokenService {

    /**
     * 自定义令牌标识
     */
    @Value("${token.header}")
    private String header;

    /**
     * 令牌秘钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * 自动刷新token的时间，当过期时间不足autoRefreshTime的值的时候，会触发刷新用户登录缓存的时间
     * 比如这个值是20,   用户是8点登录的， 8点半缓存会过期， 当过8.10分的时候，就少于20分钟了，便触发
     * 刷新登录用户的缓存时间
     */
    @Value("${token.autoRefreshTime}")
    private long autoRefreshTime;

    @NonNull
    private RedisCacheService redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserDetail getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getTokenFromRequest(request);
        if (StrUtil.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.Token.LOGIN_USER_KEY);

                return redisCache.loginUserCache.getObjectOnlyInCacheById(uuid);
            } catch (SignatureException | MalformedJwtException | UnsupportedJwtException |
                     IllegalArgumentException jwtException) {
                log.error("parse token failed. due to:{}", jwtException.getMessage());
                throw new ApiException(jwtException, ErrorCode.Internal.INVALID_TOKEN);
            } catch (Exception e) {
                log.error("fail to get cached user from redis", e);
                throw new ApiException(e, ErrorCode.UNKNOWN_ERROR);
            }

        }
        return null;
    }

    /**
     * 创建令牌
     *
     * @param loginUserDetail 用户信息
     * @return 令牌
     */
    public String createTokenAndPutUserInCache(LoginUserDetail loginUserDetail) {
        loginUserDetail.setCachedKey(IdUtil.fastUUID());

        redisCache.loginUserCache.set(loginUserDetail.getCachedKey(), loginUserDetail);

        return generateToken(MapUtil.of(Constants.Token.LOGIN_USER_KEY, loginUserDetail.getCachedKey()));
    }

    /**
     * 当超过20分钟，自动刷新token
     *
     * @param loginUserDetail 登录用户
     */
    public void refreshToken(LoginUserDetail loginUserDetail) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > loginUserDetail.getAutoRefreshCacheTime()) {
            loginUserDetail.setAutoRefreshCacheTime(currentTime + TimeUnit.MINUTES.toMillis(autoRefreshTime));
            // 根据uuid将loginUser存入缓存
            redisCache.loginUserCache.set(loginUserDetail.getCachedKey(), loginUserDetail);
        }
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    private String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @return token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StrUtil.isNotEmpty(token) && token.startsWith(Token.TOKEN_PREFIX)) {
            token = StrUtil.stripIgnoreCase(token, Token.TOKEN_PREFIX, null);
        }
        return token;
    }

}
