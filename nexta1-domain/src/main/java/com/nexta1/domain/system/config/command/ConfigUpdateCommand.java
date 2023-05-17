package com.nexta1.domain.system.config.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author valarchie
 */
@Data
@Schema
public class ConfigUpdateCommand {

    @NotNull
    @Positive
//    @Digits(integer = 0, fraction = 0, message = "参数必须是一个整数")
    private Long configId;


    @NotEmpty
    private String configValue;

}
