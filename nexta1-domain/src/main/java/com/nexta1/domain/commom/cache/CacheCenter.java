package com.nexta1.domain.commom.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.nexta1.core.cache.guava.AbstractGuavaCacheDao;
import com.nexta1.core.cache.guava.GuavaCacheService;
import com.nexta1.core.cache.redis.RedisCacheDao;
import com.nexta1.core.cache.redis.RedisCacheService;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import com.nexta1.core.web.domain.login.VO.RoleInfoVO;
import com.nexta1.orm.system.entity.SysDeptEntity;
import com.nexta1.orm.system.entity.SysRoleEntity;
import com.nexta1.orm.system.entity.SysUserEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 缓存中心  提供全局访问点
 * 如果是领域类的缓存  可以自己新建一个直接放在CacheCenter   不用放在infrastructure包里的GuavaCacheService
 * 或者RedisCacheService
 *
 * @author valarchie
 */
@Component
public class CacheCenter {

    public static AbstractGuavaCacheDao<String> configCache;

    public static AbstractGuavaCacheDao<SysDeptEntity> deptCache;

    public static RedisCacheDao<String> captchaCache;

    public static RedisCacheDao<LoginUserDetail> loginUserCache;

    public static RedisCacheDao<SysUserEntity> userCache;

    public static RedisCacheDao<SysRoleEntity> roleCache;

    public static RedisCacheDao<RoleInfoVO> roleModelInfoCache;

    @PostConstruct
    public void init() {
        GuavaCacheService guavaCache = SpringUtil.getBean(GuavaCacheService.class);
        RedisCacheService redisCache = SpringUtil.getBean(RedisCacheService.class);

        configCache = guavaCache.configCache;
        deptCache = guavaCache.deptCache;

        captchaCache = redisCache.captchaCache;
        loginUserCache = redisCache.loginUserCache;
        userCache = redisCache.userCache;
        roleCache = redisCache.roleCache;
        roleModelInfoCache = redisCache.roleModelInfoCache;
    }

}
