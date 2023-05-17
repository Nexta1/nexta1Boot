package com.nexta1.domain.system.config.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nexta1.orm.common.query.AbstractPageQuery;
import com.nexta1.orm.system.entity.SysConfigEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Schema(name = "配置查询参数")
public class ConfigQuery extends AbstractPageQuery<SysConfigEntity> {

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置key")
    private String configKey;

    @Schema(description = "是否允许更改配置")
    private Boolean isAllowChange;


    @Override
    public LambdaQueryWrapper<SysConfigEntity> toQueryWrapper() {
        LambdaQueryWrapper<SysConfigEntity> queryWrapper = new LambdaQueryWrapper<SysConfigEntity>()
                .like(StrUtil.isNotEmpty(configName), SysConfigEntity::getConfigName, configName)
                .eq(StrUtil.isNotEmpty(configKey), SysConfigEntity::getConfigKey, configKey)
                .eq(isAllowChange != null, SysConfigEntity::getIsAllowChange, isAllowChange);

        addTimeCondition(queryWrapper, SysConfigEntity::getCreateTime);

        return queryWrapper;
    }
}
