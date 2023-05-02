package com.nexta1.admin.controller.common;


import cn.hutool.core.util.StrUtil;
import com.nexta1.common.config.Nexta1BootConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private Nexta1BootConfig nexta1BootConfig;

    @GetMapping("/")
    @Operation(summary = "首页")
    public String Home() {
        return StrUtil.format("{}", "Hello World", nexta1BootConfig.getName());
    }
}
