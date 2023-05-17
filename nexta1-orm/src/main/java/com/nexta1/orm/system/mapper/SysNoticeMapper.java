package com.nexta1.orm.system.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Description:
 * 通知
 *
 * @author nexta1
 * @date 2023/5/16 23:26
 */
public interface SysNoticeMapper extends BaseMapper<SysNoticeEntity> {


    @Select("SELECT n.* "
            + "FROM sys_notice n "
            + "LEFT JOIN sys_user u ON n.creator_id = u.user_id "
            + "${ew.customSqlSegment}")
    Page<SysNoticeEntity> getNoticeList(Page<SysNoticeEntity> page,
                                        @Param(Constants.WRAPPER) Wrapper<SysNoticeEntity> queryWrapper);

}
