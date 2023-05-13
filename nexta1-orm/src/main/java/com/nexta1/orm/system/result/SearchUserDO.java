package com.nexta1.orm.system.result;


import com.nexta1.orm.system.entity.SysUserEntity;
import lombok.Data;

/**
 * 如果Entity的字段和复杂查询不匹配时   自定义类来接收
 *
 * @author valarchie
 */
@Data
public class SearchUserDO extends SysUserEntity {

    private String deptName;
    private String deptLeader;

}
