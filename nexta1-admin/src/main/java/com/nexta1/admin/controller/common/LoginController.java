package com.nexta1.admin.controller.common;


import cn.hutool.core.util.StrUtil;
import com.nexta1.common.config.Nexta1BootConfig;
import io.swagger.v3.oas.annotations.Operation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    @NonNull
    private Nexta1BootConfig nexta1BootConfig;

    @GetMapping("/")
    @Operation(summary = "首页")
    public String Home() {
        log.info("{}", nexta1BootConfig.getName());
        return StrUtil.format("{},Hello World{}", nexta1BootConfig.getVersion(), nexta1BootConfig.getName());
    }
}
