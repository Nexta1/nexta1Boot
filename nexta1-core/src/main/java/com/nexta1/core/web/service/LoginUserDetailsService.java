package com.nexta1.core.web.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import com.nexta1.core.config.SecurityConfig;
import com.nexta1.core.util.BasicEnumUtil;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import com.nexta1.core.web.domain.login.VO.RoleInfoVO;
import com.nexta1.orm.common.enums.DataScopeEnum;
import com.nexta1.orm.common.enums.UserStatusEnum;
import com.nexta1.orm.system.entity.SysMenuEntity;
import com.nexta1.orm.system.entity.SysRoleEntity;
import com.nexta1.orm.system.entity.SysUserEntity;
import com.nexta1.orm.system.service.ISysMenuService;
import com.nexta1.orm.system.service.ISysRoleService;
import com.nexta1.orm.system.service.ISysUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 自定义加载用户信息通过用户名
 * 用于SpringSecurity 登录流程
 *
 * @author valarchie
 * @see SecurityConfig
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    @NonNull
    private ISysUserService userService;

    @NonNull
    private ISysMenuService menuService;

    @NonNull
    private ISysRoleService roleService;

    @NonNull
    private TokenService tokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity userEntity = userService.getUserByUserName(username);
        if (userEntity == null) {
            log.info("登录用户：{} 不存在.", username);
            throw new ApiException(ErrorCode.Business.USER_NON_EXIST, username);
        }
        if (!Objects.equals(UserStatusEnum.NORMAL.getValue(), userEntity.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new ApiException(ErrorCode.Business.USER_IS_DISABLE, username);
        }
        LoginUserDetail loginUserDetail = new LoginUserDetail(userEntity.getUserId(), userEntity.getIsAdmin(), userEntity.getUsername(),
                userEntity.getPassword());
        loginUserDetail.setLoginTime(System.currentTimeMillis());
        loginUserDetail.setAutoRefreshCacheTime(loginUserDetail.getLoginTime() + TimeUnit.MINUTES.toMillis(tokenService.getAutoRefreshTime()));
        loginUserDetail.fillUserAgent();
        return loginUserDetail;
    }

    public RoleInfoVO getRoleInfo(Long roleId) {
        if (roleId == null) {
            return RoleInfoVO.EMPTY_ROLE;
        }

        if (roleId == RoleInfoVO.ADMIN_ROLE_ID) {

            List<SysMenuEntity> allMenus = menuService.lambdaQuery().select(SysMenuEntity::getMenuId).list();

            Set<Long> allMenuIds = allMenus.stream().map(SysMenuEntity::getMenuId).collect(Collectors.toSet());

            return new RoleInfoVO(RoleInfoVO.ADMIN_ROLE_ID, RoleInfoVO.ADMIN_ROLE_KEY, DataScopeEnum.ALL, SetUtils.emptySet(),
                    RoleInfoVO.ADMIN_PERMISSIONS, allMenuIds);

        }

        SysRoleEntity roleEntity = roleService.getById(roleId);

        if (roleEntity == null) {
            return RoleInfoVO.EMPTY_ROLE;
        }

        List<SysMenuEntity> menuList = roleService.getMenuListByRoleId(roleId);

        Set<Long> menuIds = menuList.stream().map(SysMenuEntity::getMenuId).collect(Collectors.toSet());
        Set<String> permissions = menuList.stream().map(SysMenuEntity::getPerms).collect(Collectors.toSet());

        DataScopeEnum dataScopeEnum = BasicEnumUtil.fromValue(DataScopeEnum.class, roleEntity.getDataScope());

        Set<Long> deptIdSet = SetUtils.emptySet();
        if (StrUtil.isNotEmpty(roleEntity.getDeptIdSet())) {
            deptIdSet = StrUtil.split(roleEntity.getDeptIdSet(), ",").stream()
                    .map(Convert::toLong).collect(Collectors.toSet());
        }

        return new RoleInfoVO(roleId, roleEntity.getRoleKey(), dataScopeEnum, deptIdSet, permissions, menuIds);
    }


}
