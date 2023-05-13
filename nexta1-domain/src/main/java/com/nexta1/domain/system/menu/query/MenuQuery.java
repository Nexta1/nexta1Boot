package com.nexta1.domain.system.menu.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexta1.orm.common.query.AbstractQuery;
import com.nexta1.orm.system.entity.SysMenuEntity;
import lombok.Data;

import java.util.Arrays;

/**
 * @author valarchie
 */
@Data
public class MenuQuery extends AbstractQuery<SysMenuEntity> {

    private String menuName;
    private Boolean isVisible;
    private Integer status;


    @Override
    public QueryWrapper<SysMenuEntity> toQueryWrapper() {
        QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<SysMenuEntity>()
                .like(StrUtil.isNotEmpty(menuName), "menu_name", menuName)
                .eq(isVisible != null, "is_visible", isVisible)
                .eq(status != null, "status", status);

        queryWrapper.orderBy(true, true, Arrays.asList("parent_id", "order_num"));
        return queryWrapper;
    }
}
