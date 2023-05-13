package com.nexta1.core.cache.redis;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 * 缓存
 *
 * @author nexta1
 * @date 2023/5/3 13:08
 */
public enum CacheKeyEnum {

    /**
     * Redis各类缓存集合
     */
    CAPTCHA("captcha_codes:", 2, TimeUnit.MINUTES),
    LOGIN_USER_KEY("login_tokens:", 30, TimeUnit.MINUTES),
    RATE_LIMIT_KEY("rate_limit:", 60, TimeUnit.SECONDS),
    USER_ENTITY_KEY("user_entity:", 60, TimeUnit.MINUTES),
    ROLE_ENTITY_KEY("role_entity:", 60, TimeUnit.MINUTES),
    ROLE_MODEL_INFO_KEY("role_model_info:", 60, TimeUnit.MINUTES),

    ;


    CacheKeyEnum(String key, int expiration, TimeUnit timeUnit) {
        this.key = key;
        this.expiration = expiration;
        this.timeUnit = timeUnit;
    }

    private final String key;
    private final int expiration;
    private final TimeUnit timeUnit;

    public String key() {
        return key;
    }

    public int expiration() {
        return expiration;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }

}
