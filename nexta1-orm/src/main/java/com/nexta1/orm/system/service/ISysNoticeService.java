package com.nexta1.orm.system.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 * 通知
 *
 * @author nexta1
 * @date 2023/5/16 23:25
 */
public interface ISysNoticeService extends IService<SysNoticeEntity> {

    /**
     * 获取公告列表
     *
     * @param page         页码对象
     * @param queryWrapper 查询对象
     * @return
     */
    Page<SysNoticeEntity> getNoticeList(Page<SysNoticeEntity> page,
                                        @Param(Constants.WRAPPER) Wrapper<SysNoticeEntity> queryWrapper);

}
