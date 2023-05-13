package com.nexta1.domain.commom.dto;

import com.nexta1.domain.system.user.dto.UserDTO;
import com.nexta1.orm.common.result.DictionaryData;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author valarchie
 */
@Data
public class UserPermissionDTO {

    private UserDTO user;
    private String roleKey;
    private Set<String> permissions;
    private Map<String, List<DictionaryData>> dictTypes;

}
