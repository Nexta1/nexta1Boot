package com.nexta1.core.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nexta1.core.security.AuthenticationUtils;
import com.nexta1.core.web.domain.login.DTO.RoleInfo;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author valarchie
 */
@Service("permission")
public class MenuPermissionService {


    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean has(String permission) {
        if (StrUtil.isEmpty(permission)) {
            return false;
        }
        LoginUserDetail loginUser = AuthenticationUtils.getLoginUser();
        if (loginUser == null || CollUtil.isEmpty(loginUser.getRoleInfo().getMenuPermissions())) {
            return false;
        }
        return has(loginUser.getRoleInfo().getMenuPermissions(), permission);
    }


    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean has(Set<String> permissions, String permission) {
        return permissions.contains(RoleInfo.ALL_PERMISSIONS) || permissions.contains(StrUtil.trim(permission));
    }

}
