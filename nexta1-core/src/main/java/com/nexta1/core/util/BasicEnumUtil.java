package com.nexta1.core.util;

import cn.hutool.core.convert.Convert;
import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import com.nexta1.orm.common.interfaces.BasicEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author valarchie
 */
public class BasicEnumUtil {

    public static final String UNKNOWN = "未知";

    public static <E extends Enum<E>, V> E fromValueSafely(Class<E> enumClass, V value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumConstant -> Objects.equals(((BasicEnum<?>) enumConstant).getValue(), value))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.Internal.GET_ENUM_FAILED, enumClass.getSimpleName()));

    }

    public static <E extends Enum<E>, V> E fromValue(Class<E> enumClass, V value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(enumConstant -> Objects.equals(((BasicEnum<?>) enumConstant).getValue(), value))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.Internal.GET_ENUM_FAILED, enumClass.getSimpleName()));
    }

    public static <E extends Enum<E>> String getDescriptionByBool(Class<E> enumClass, Boolean bool) {
        Integer value = Convert.toInt(bool, 0);
        return getDescriptionByValue(enumClass, value);
    }

    public static <E extends Enum<E>> String getDescriptionByValue(Class<E> enumClass, Object value) {
        E basicEnum = fromValueSafely(enumClass, value);
        if (basicEnum != null) {
            return ((BasicEnum<?>) basicEnum).description();
        }
        return UNKNOWN;
    }

}
