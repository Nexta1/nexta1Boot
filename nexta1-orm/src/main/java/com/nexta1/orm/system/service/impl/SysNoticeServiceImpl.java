package com.nexta1.orm.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import com.nexta1.orm.system.mapper.SysNoticeMapper;
import com.nexta1.orm.system.service.ISysNoticeService;
import org.springframework.stereotype.Service;

/**
 * Description:
 * 通知
 *
 * @author nexta1
 * @date 2023/5/16 23:28
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNoticeEntity> implements ISysNoticeService {

    @Override
    public Page<SysNoticeEntity> getNoticeList(Page<SysNoticeEntity> page, Wrapper<SysNoticeEntity> queryWrapper) {
        return this.baseMapper.getNoticeList(page, queryWrapper);
    }

}
