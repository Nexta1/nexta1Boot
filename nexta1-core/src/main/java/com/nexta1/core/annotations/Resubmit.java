package com.nexta1.core.annotations;

import java.lang.annotation.*;

/**
 * 自定义注解防止表单重复提交
 * 仅生效于有RequestBody注解的参数  因为使用RequestBodyAdvice来实现
 *
 * @author valarchie
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Resubmit {

    /**
     * 间隔时间(s)，小于此时间视为重复提交
     */
    int interval() default 5;

}
