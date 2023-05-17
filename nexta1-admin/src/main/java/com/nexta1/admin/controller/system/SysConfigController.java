package com.nexta1.admin.controller.system;

import com.nexta1.common.core.dto.ResponseDTO;
import com.nexta1.common.core.page.PageDTO;
import com.nexta1.core.annotations.AccessLog;
import com.nexta1.domain.commom.cache.CacheCenter;
import com.nexta1.domain.system.config.ConfigApplicationService;
import com.nexta1.domain.system.config.command.ConfigUpdateCommand;
import com.nexta1.domain.system.config.dto.ConfigDTO;
import com.nexta1.domain.system.config.query.ConfigQuery;
import com.nexta1.orm.common.enums.BusinessTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Description: 参数配置
 *
 * @author nexta1
 * @date 2023/5/14 13:48
 */
@Tag(name = "参数配置", description = "参数配置控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/config")
@Validated
public class SysConfigController {
    @NonNull
    public ConfigApplicationService configApplicationService;

    /**
     * 获取参数配置列表
     */
    @Operation(summary = "配置列表", description = "分页获取配置列表")
    @PreAuthorize("@permission.has('system:config:list')")
    @GetMapping("/list")
    public ResponseDTO<PageDTO<ConfigDTO>> list(@Valid ConfigQuery query) {
        PageDTO<ConfigDTO> page = configApplicationService.getConfigList(query);
        return ResponseDTO.ok(page);
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@permission.has('system:config:edit')")
    @AccessLog(title = "参数管理", businessType = BusinessTypeEnum.MODIFY)
    @Operation(summary = "配置修改", description = "配置修改")
    @PutMapping
    public ResponseDTO<Void> edit(@RequestBody @Valid ConfigUpdateCommand config) {
        configApplicationService.updateConfig(config);
        return ResponseDTO.ok();
    }

    /**
     * 刷新参数缓存
     */
    @Operation(summary = "刷新配置缓存")
    @PreAuthorize("@permission.has('system:config:remove')")
    @AccessLog(title = "参数管理", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/refreshCache")
    public ResponseDTO<Void> refreshCache() {
        CacheCenter.configCache.invalidateAll();
        return ResponseDTO.ok();
    }


}
