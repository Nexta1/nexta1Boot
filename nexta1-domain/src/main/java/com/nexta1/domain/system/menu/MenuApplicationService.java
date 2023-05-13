package com.nexta1.domain.system.menu;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.nexta1.core.web.domain.login.VO.LoginUserDetail;
import com.nexta1.domain.system.menu.DTO.MenuDTO;
import com.nexta1.domain.system.menu.DTO.RouterDTO;
import com.nexta1.domain.system.menu.model.MenuModelFactory;
import com.nexta1.domain.system.menu.model.RouterModel;
import com.nexta1.domain.system.menu.query.MenuQuery;
import com.nexta1.orm.common.enums.MenuTypeEnum;
import com.nexta1.orm.system.entity.SysMenuEntity;
import com.nexta1.orm.system.service.ISysMenuService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单应用服务
 *
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MenuApplicationService {

    @NonNull
    private ISysMenuService menuService;

    @NonNull
    private MenuModelFactory menuModelFactory;


    public List<MenuDTO> getMenuList(MenuQuery query) {
        List<SysMenuEntity> list = menuService.list(query.toQueryWrapper());
        return list.stream().map(MenuDTO::new).collect(Collectors.toList());
    }

    public MenuDTO getMenuInfo(Long menuId) {
        SysMenuEntity byId = menuService.getById(menuId);
        return new MenuDTO(byId);
    }

    public List<Tree<Long>> getDropdownList(LoginUserDetail loginUser) {
        List<SysMenuEntity> menuEntityList =
                loginUser.isAdmin() ? menuService.list() : menuService.getMenuListByUserId(loginUser.getUserId());

        return buildMenuTreeSelect(menuEntityList);
    }


//    public void addMenu(AddMenuCommand addCommand) {
//        MenuModel model = menuModelFactory.create();
//        model.loadAddCommand(addCommand);
//
//        model.checkMenuNameUnique();
//        model.checkExternalLink();
//
//        model.insert();
//    }
//
//    public void updateMenu(UpdateMenuCommand updateCommand) {
//        MenuModel model = menuModelFactory.loadById(updateCommand.getMenuId());
//        model.loadUpdateCommand(updateCommand);
//
//        model.checkMenuNameUnique();
//        model.checkExternalLink();
//        model.checkParentIdConflict();
//
//        model.updateById();
//    }
//
//
//    public void remove(Long menuId) {
//        MenuModel menuModel = menuModelFactory.loadById(menuId);
//
//        menuModel.checkHasChildMenus();
//        menuModel.checkMenuAlreadyAssignToRole();
//
//        menuModel.deleteById();
//    }


    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<Tree<Long>> buildMenuTreeSelect(List<SysMenuEntity> menus) {
        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("menuId");
        return TreeUtil.build(menus, 0L, config, (menu, tree) -> {
            // 也可以使用 tree.setId(dept.getId());等一些默认值
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.putExtra("label", menu.getMenuName());
        });
    }


    public List<Tree<Long>> buildMenuEntityTree(LoginUserDetail loginUser) {
        List<SysMenuEntity> allMenus;
        if (loginUser.isAdmin()) {
            allMenus = menuService.list();
        } else {
            allMenus = menuService.getMenuListByUserId(loginUser.getUserId());
        }

        //过滤按钮
        List<SysMenuEntity> noButtonMenus = allMenus.stream()
                .filter(menu -> !MenuTypeEnum.BUTTON.getValue().equals(menu.getMenuType()))
                .collect(Collectors.toList());

        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("menuId");
        config.setWeightKey("orderNum");

        return TreeUtil.build(noButtonMenus, 0L, config, (menu, tree) -> {
            // 也可以使用 tree.setId(dept.getId());等一些默认值
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.setWeight(menu.getOrderNum());
            tree.putExtra("entity", menu);
        });

    }


    public List<RouterDTO> buildRouterTree(List<Tree<Long>> trees) {
        List<RouterDTO> routers = new LinkedList<>();
        if (CollUtil.isNotEmpty(trees)) {
            for (Tree<Long> tree : trees) {
                RouterDTO routerDTO;

                Object entity = tree.get("entity");

                if (entity != null) {
                    RouterModel model = new RouterModel();
                    BeanUtil.copyProperties(entity, model);

                    routerDTO = model.produceDefaultRouterVO();

                    if (model.isMultipleLevelMenu(tree)) {
                        routerDTO = model.produceMultipleLevelMenuRouterVO(buildRouterTree(tree.getChildren()));
                    }

                    if (model.isSingleLevelMenu()) {
                        routerDTO = model.produceSingleLevelMenuRouterVO();
                    }

                    if (model.isTopInnerLink()) {
                        routerDTO = model.produceInnerLinkRouterVO();
                    }

                    routers.add(routerDTO);
                }

            }
        }

        return routers;
    }


    public List<RouterDTO> getRouterTree(LoginUserDetail loginUser) {
        List<Tree<Long>> trees = buildMenuEntityTree(loginUser);
        return buildRouterTree(trees);
//        return trees;
    }


}
