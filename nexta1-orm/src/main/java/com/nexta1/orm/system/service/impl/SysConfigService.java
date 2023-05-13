package com.nexta1.orm.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nexta1.orm.system.entity.SysConfigEntity;
import com.nexta1.orm.system.mapper.SysConfigMapper;
import com.nexta1.orm.system.service.ISysConfigService;
import org.springframework.stereotype.Service;

/**
 * Description: 配置实现
 *
 * @author nexta1
 * @date 2023/5/3 15:46
 */
@Service
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements ISysConfigService {
    @Override
    public String getConfigValueByKey(String key) {
        return lambdaQuery()
                .eq(key != null && !key.equals(""), SysConfigEntity::getConfigKey, key)
                .one()
                .getConfigValue();
    }
}
