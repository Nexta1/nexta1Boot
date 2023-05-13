package com.nexta1.core.web.domain.login.DTO;

import lombok.Data;

/**
 * Description:
 * LoginDto
 *
 * @author nexta1
 * @date 2023/5/3 22:18
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

}
