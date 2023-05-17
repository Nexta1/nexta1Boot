package com.nexta1.orm.common.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.nexta1.common.utils.time.DatePickUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author valarchie
 */
@Data
public abstract class AbstractQuery<T> {


    protected String isAsc;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private Date beginTime;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endTime;

    private static final String ASC = "ascending";
    private static final String DESC = "descending";

    /**
     * 生成query conditions
     */
    public abstract Wrapper<T> toQueryWrapper();

    public void addSortCondition(LambdaQueryWrapper<T> queryWrapper, String orderByColumn, SFunction<T, ?> columnFunc) {
        if (queryWrapper != null && StringUtils.isNotBlank(orderByColumn) && columnFunc != null) {
            boolean sortDirection = convertSortDirection();
            queryWrapper.orderBy(true, sortDirection, columnFunc);
        }
    }

    public void addTimeCondition(LambdaQueryWrapper<T> queryWrapper, SFunction<T, ?> columnFunc) {
        if (queryWrapper != null) {
            queryWrapper
                    .ge(beginTime != null, columnFunc, DatePickUtil.getBeginOfTheDay(beginTime))
                    .le(endTime != null, columnFunc, DatePickUtil.getEndOfTheDay(endTime));
        }
    }

    public boolean convertSortDirection() {
        return !StrUtil.isNotEmpty(isAsc) || !DESC.equals(isAsc);
    }

}
