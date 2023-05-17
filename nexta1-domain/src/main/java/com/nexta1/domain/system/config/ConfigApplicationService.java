package com.nexta1.domain.system.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nexta1.common.core.page.PageDTO;
import com.nexta1.domain.commom.cache.CacheCenter;
import com.nexta1.domain.system.config.command.ConfigUpdateCommand;
import com.nexta1.domain.system.config.dto.ConfigDTO;
import com.nexta1.domain.system.config.model.ConfigModel;
import com.nexta1.domain.system.config.model.ConfigModelFactory;
import com.nexta1.domain.system.config.query.ConfigQuery;
import com.nexta1.orm.system.entity.SysConfigEntity;
import com.nexta1.orm.system.service.ISysConfigService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class ConfigApplicationService {

    @NonNull
    private ConfigModelFactory configModelFactory;

    @NonNull
    private ISysConfigService configService;

    public PageDTO<ConfigDTO> getConfigList(ConfigQuery query) {
        Page<SysConfigEntity> page = configService.page(query.toPage(), query.toQueryWrapper());
        List<ConfigDTO> records = page.getRecords().stream().map(ConfigDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public ConfigDTO getConfigInfo(Long id) {
        SysConfigEntity byId = configService.getById(id);
        return new ConfigDTO(byId);
    }

    public void updateConfig(ConfigUpdateCommand updateCommand) {
        ConfigModel configModel = configModelFactory.loadById(updateCommand.getConfigId());
        configModel.loadUpdateCommand(updateCommand);

        configModel.checkCanBeModify();

        configModel.updateById();

        CacheCenter.configCache.invalidate(configModel.getConfigKey());
    }


}
