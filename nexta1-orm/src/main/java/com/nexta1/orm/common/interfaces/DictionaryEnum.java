package com.nexta1.orm.common.interfaces;

/**
 * 字典类型 接口
 *
 * @author valarchie
 */
public interface DictionaryEnum<T> extends BasicEnum<T> {

    /**
     * 获取css标签
     *
     * @return css标签
     */
    String cssTag();

}
