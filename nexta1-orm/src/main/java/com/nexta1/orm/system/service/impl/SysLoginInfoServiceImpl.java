package com.nexta1.orm.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nexta1.orm.system.entity.SysLoginInfoEntity;
import com.nexta1.orm.system.mapper.SysLoginInfoMapper;
import com.nexta1.orm.system.service.ISysLoginInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-07-10
 */
@Service
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoMapper, SysLoginInfoEntity> implements ISysLoginInfoService {

}
