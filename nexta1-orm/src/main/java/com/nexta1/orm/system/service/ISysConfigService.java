package com.nexta1.orm.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nexta1.orm.system.entity.SysConfigEntity;

/**
 * Description: 参数配置
 *
 * @author nexta1
 * @date 2023/5/3 12:26
 */
public interface ISysConfigService extends IService<SysConfigEntity> {
    String getConfigValueByKey(String key);
}
