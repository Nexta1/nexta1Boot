package com.nexta1.orm.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nexta1.orm.system.entity.SysOperationLogEntity;
import com.nexta1.orm.system.mapper.SysOperationLogMapper;
import com.nexta1.orm.system.service.ISysOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-08
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLogEntity> implements
        ISysOperationLogService {

}
