package com.nexta1.core.annotations;


import com.nexta1.orm.common.enums.BusinessTypeEnum;
import com.nexta1.orm.common.enums.OperatorTypeEnum;

import java.lang.annotation.*;

/**
 * Description:
 * 自定义日志注解
 *
 * @author nexta1
 * @date 2023/5/15 21:05
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLog {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    /**
     * 操作人类别
     */
    OperatorTypeEnum operatorType() default OperatorTypeEnum.WEB;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default false;
}
