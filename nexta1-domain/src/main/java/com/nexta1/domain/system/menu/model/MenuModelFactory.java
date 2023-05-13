package com.nexta1.domain.system.menu.model;

import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import com.nexta1.orm.system.entity.SysMenuEntity;
import com.nexta1.orm.system.service.ISysMenuService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 菜单模型工厂
 *
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class MenuModelFactory {

    @NonNull
    private ISysMenuService menuService;

    public MenuModel loadById(Long menuId) {
        SysMenuEntity byId = menuService.getById(menuId);
        if (byId == null) {
            throw new ApiException(ErrorCode.Business.OBJECT_NOT_FOUND, menuId, "菜单");
        }
        return new MenuModel(byId, menuService);
    }

    public MenuModel create() {
        return new MenuModel(menuService);
    }


}
