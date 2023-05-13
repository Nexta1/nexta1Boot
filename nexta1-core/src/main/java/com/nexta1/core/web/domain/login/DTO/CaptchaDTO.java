package com.nexta1.core.web.domain.login.DTO;

import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class CaptchaDTO {

    private Boolean isCaptchaOn;
    private String uuid;
    private String img;

}
