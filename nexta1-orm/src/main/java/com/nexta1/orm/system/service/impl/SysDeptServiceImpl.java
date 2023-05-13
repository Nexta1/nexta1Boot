package com.nexta1.orm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nexta1.orm.system.entity.SysDeptEntity;
import com.nexta1.orm.system.entity.SysUserEntity;
import com.nexta1.orm.system.mapper.SysDeptMapper;
import com.nexta1.orm.system.mapper.SysUserMapper;
import com.nexta1.orm.system.service.ISysDeptService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptEntity> implements ISysDeptService {

    @NonNull
    private SysUserMapper userMapper;


    @Override
    public boolean isDeptNameDuplicated(String deptName, Long deptId, Long parentId) {
        QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_name", deptName)
                .ne(deptId != null, "dept_id", deptId)
                .eq(parentId != null, "parent_id", parentId);

        return this.baseMapper.exists(queryWrapper);
    }


    @Override
    public boolean hasChildrenDept(Long deptId, Boolean enabled) {
        QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(enabled != null, "status", 1)
                .and(o -> o.eq("parent_id", deptId).or()
                        .apply("FIND_IN_SET (" + deptId + " , ancestors)")
                );
        return this.baseMapper.exists(queryWrapper);
    }


    @Override
    public boolean isChildOfTheDept(Long parentId, Long childId) {
        QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("dept_id = '" + childId + "' and FIND_IN_SET ( " + parentId + " , ancestors)");
        return this.baseMapper.exists(queryWrapper);
    }


    @Override
    public boolean isDeptAssignedToUsers(Long deptId) {
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        return userMapper.exists(queryWrapper);
    }

}
