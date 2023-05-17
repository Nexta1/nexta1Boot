package com.nexta1.orm.common.interfaces;

/**
 * Description:
 * 枚举接口
 *
 * @author nexta1
 * @date 2023/5/15 22:22
 */
public interface BasicEnum<T> {


    /**
     * 获取枚举的值
     *
     * @return 枚举值
     */
    T getValue();

    /**
     * 获取枚举的描述
     *
     * @return 描述
     */
    String description();


}
