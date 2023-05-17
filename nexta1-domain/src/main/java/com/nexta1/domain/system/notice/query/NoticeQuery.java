package com.nexta1.domain.system.notice.query;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexta1.orm.common.query.AbstractPageQuery;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Schema(name = "公告查询参数")
public class NoticeQuery extends AbstractPageQuery<SysNoticeEntity> {

    @Schema(description = "公告标题")
    private String noticeTitle;

    @Schema(description = "操作人员")
    private String creatorName;

    @Schema(description = "类型")
    private Integer noticeType;


    @Override
    public QueryWrapper<SysNoticeEntity> toQueryWrapper() {
        return new QueryWrapper<SysNoticeEntity>()
                .like(StrUtil.isNotEmpty(noticeTitle), "notice_title", noticeTitle)
                .eq(noticeType != null, "notice_type", noticeType)
                .eq("n.deleted", 0)
                .like(StrUtil.isNotEmpty(creatorName), "u.username", creatorName);
    }
}
