package com.nexta1.core.annotations;

import cn.hutool.extra.servlet.ServletUtil;
import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import com.nexta1.common.utils.ServletHolderUtil;
import com.nexta1.core.security.AuthenticationUtils;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author valarchie
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流key
     */
    String key() default "None";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int maxCount() default 100;

    /**
     * 限流条件类型
     */
    LimitType limitType() default LimitType.GLOBAL;

    /**
     * 限流使用的缓存类型
     */
    CacheType cacheType() default CacheType.REDIS;


    enum LimitType {
        /**
         * 默认策略全局限流  不区分IP和用户
         */
        GLOBAL {
            @Override
            public String generateCombinedKey(RateLimit rateLimiter) {
                return rateLimiter.key() + this.name();
            }
        },

        /**
         * 根据请求者IP进行限流
         */
        IP {
            @Override
            public String generateCombinedKey(RateLimit rateLimiter) {
                String clientIP = ServletUtil.getClientIP(ServletHolderUtil.getRequest());
                return rateLimiter.key() + clientIP;
            }
        },

        /**
         * 按用户限流
         */
        USER {
            @Override
            public String generateCombinedKey(RateLimit rateLimiter) {
                LoginUserDetail loginUser = AuthenticationUtils.getLoginUser();
                if (loginUser == null) {
                    throw new ApiException(ErrorCode.Client.COMMON_NO_AUTHORIZATION);
                }
                return rateLimiter.key() + loginUser.getUsername();
            }
        };

        public abstract String generateCombinedKey(RateLimit rateLimiter);

    }

    enum CacheType {

        /**
         * 使用redis做缓存
         */
        REDIS,

        /**
         * 使用map做缓存
         */
        Map

    }

}
