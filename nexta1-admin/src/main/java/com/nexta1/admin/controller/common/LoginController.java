package com.nexta1.admin.controller.common;


import cn.hutool.core.util.StrUtil;
import com.nexta1.common.config.Nexta1BootConfig;
import com.nexta1.common.core.dto.ResponseDTO;
import com.nexta1.core.cache.map.MapCache;
import com.nexta1.core.security.AuthenticationUtils;
import com.nexta1.core.web.domain.login.DTO.CaptchaDTO;
import com.nexta1.core.web.domain.login.DTO.LoginDTO;
import com.nexta1.core.web.domain.login.DTO.TokenDTO;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import com.nexta1.core.web.service.LoginService;
import com.nexta1.domain.commom.cache.CacheCenter;
import com.nexta1.domain.commom.dto.UserPermissionDTO;
import com.nexta1.domain.system.menu.DTO.RouterDTO;
import com.nexta1.domain.system.menu.MenuApplicationService;
import com.nexta1.domain.system.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    @NonNull
    private LoginService loginService;

    @NonNull
    private Nexta1BootConfig nexta1BootConfig;

    @NonNull
    private MenuApplicationService menuApplicationService;

    @GetMapping("/")
    @Operation(summary = "首页")
    public String Home() {
        log.info("{}", nexta1BootConfig.getName());
        return StrUtil.format("{},Hello World{}", nexta1BootConfig.getVersion(), nexta1BootConfig.getName());
    }

    @Operation(summary = "验证码")
    @GetMapping("/captchaImage")
    public ResponseDTO<CaptchaDTO> getCaptchaImg() {
        CaptchaDTO captchaImg = loginService.generateCaptchaImg();
        return ResponseDTO.ok(captchaImg);
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseDTO<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        // 生成令牌
        String token = loginService.login(loginDTO);

        return ResponseDTO.ok(new TokenDTO(token));
    }

    @Operation(summary = "获取用户对应的菜单路由", description = "用于动态生成路由")
    @GetMapping("/getRouters")
    public ResponseDTO<List<RouterDTO>> getRouters() {
        LoginUserDetail loginUser = AuthenticationUtils.getLoginUser();
        List<RouterDTO> routerTree = menuApplicationService.getRouterTree(loginUser);
        return ResponseDTO.ok(routerTree);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/getLoginUserInfo")
    public ResponseDTO<UserPermissionDTO> getLoginUserInfo() {
        LoginUserDetail loginUser = AuthenticationUtils.getLoginUser();
        UserPermissionDTO permissionDTO = new UserPermissionDTO();
        permissionDTO.setUser(new UserDTO(CacheCenter.userCache.getObjectById(loginUser.getUserId())));
        permissionDTO.setRoleKey(loginUser.getRoleInfo().getRoleKey());
        permissionDTO.setPermissions(loginUser.getRoleInfo().getMenuPermissions());
        permissionDTO.setDictTypes(MapCache.dictionaryCache());
        return ResponseDTO.ok(permissionDTO);
    }
}
