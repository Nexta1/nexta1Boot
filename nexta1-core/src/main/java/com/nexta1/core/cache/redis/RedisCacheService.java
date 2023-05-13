package com.nexta1.core.cache.redis;

import cn.hutool.extra.spring.SpringUtil;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import com.nexta1.core.web.domain.login.VO.RoleInfoVO;
import com.nexta1.core.web.service.LoginUserDetailsService;
import com.nexta1.orm.system.entity.SysRoleEntity;
import com.nexta1.orm.system.entity.SysUserEntity;
import com.nexta1.orm.system.service.ISysRoleService;
import com.nexta1.orm.system.service.ISysUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Description:
 * Redis
 *
 * @author nexta1
 * @date 2023/5/3 16:18
 */
@Component
@RequiredArgsConstructor
public class RedisCacheService {

    @NonNull
    private RedisUtil redisUtil;

    public RedisCacheDao<String> captchaCache;
    public RedisCacheDao<LoginUserDetail> loginUserCache;
    public RedisCacheDao<SysUserEntity> userCache;
    public RedisCacheDao<SysRoleEntity> roleCache;
    public RedisCacheDao<RoleInfoVO> roleModelInfoCache;

    /**
     * 它用来修饰一个非静态的void方法。它会在服务器加载Servlet的时候运行，并且只运行一次
     */
    @PostConstruct
    public void init() {

        captchaCache = new RedisCacheDao<>(redisUtil, CacheKeyEnum.CAPTCHA);

        loginUserCache = new RedisCacheDao<>(redisUtil, CacheKeyEnum.LOGIN_USER_KEY);

        userCache = new RedisCacheDao<SysUserEntity>(redisUtil, CacheKeyEnum.USER_ENTITY_KEY) {
            @Override
            public SysUserEntity getObjectFromDb(Object id) {
                ISysUserService userService = SpringUtil.getBean(ISysUserService.class);
                return userService.getById((Serializable) id);
            }
        };

        roleCache = new RedisCacheDao<SysRoleEntity>(redisUtil, CacheKeyEnum.ROLE_ENTITY_KEY) {
            @Override
            public SysRoleEntity getObjectFromDb(Object id) {
                ISysRoleService roleService = SpringUtil.getBean(ISysRoleService.class);
                return roleService.getById((Serializable) id);
            }
        };

        roleModelInfoCache = new RedisCacheDao<>(redisUtil, CacheKeyEnum.ROLE_MODEL_INFO_KEY) {
            @Override
            public RoleInfoVO getObjectFromDb(Object id) {
                LoginUserDetailsService userDetailsService = SpringUtil.getBean(LoginUserDetailsService.class);
                return userDetailsService.getRoleInfo((Long) id);
            }

        };

    }


}
