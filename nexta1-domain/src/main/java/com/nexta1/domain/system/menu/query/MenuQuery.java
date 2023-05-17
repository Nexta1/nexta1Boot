package com.nexta1.domain.system.menu.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.nexta1.orm.common.query.AbstractQuery;
import com.nexta1.orm.system.entity.SysMenuEntity;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author valarchie
 */
@Data
public class MenuQuery extends AbstractQuery<SysMenuEntity> {

    private String menuName;
    private Boolean isVisible;
    private Integer status;


    @Override
    public LambdaQueryWrapper<SysMenuEntity> toQueryWrapper() {
        LambdaQueryWrapper<SysMenuEntity> queryWrapper = new LambdaQueryWrapper<SysMenuEntity>()
                .like(StrUtil.isNotEmpty(menuName), SysMenuEntity::getMenuName, menuName)
                .eq(isVisible != null, SysMenuEntity::getIsVisible, isVisible)
                .eq(status != null, SysMenuEntity::getStatus, status);

        List<SFunction<SysMenuEntity, ?>> orderByColumns = Arrays.asList(SysMenuEntity::getParentId, SysMenuEntity::getOrderNum);
        queryWrapper.orderBy(true, true, orderByColumns);
        return queryWrapper;
    }

}
