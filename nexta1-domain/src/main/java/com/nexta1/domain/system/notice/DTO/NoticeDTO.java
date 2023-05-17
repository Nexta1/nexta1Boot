package com.nexta1.domain.system.notice.DTO;

import com.nexta1.domain.commom.cache.CacheCenter;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import com.nexta1.orm.system.entity.SysUserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 *
 * @author nexta1
 * @date 2023/5/16 20:49
 */
@Data
@Schema(name = "NoticeDTO", description = "配置信息")
public class NoticeDTO {
    public NoticeDTO(SysNoticeEntity entity) {
        if (entity != null) {
            this.noticeId = String.valueOf(entity.getNoticeId());
            this.noticeTitle = entity.getNoticeTitle();
            this.noticeType = entity.getNoticeType();
            this.noticeContent = entity.getNoticeContent();
            this.status = entity.getStatus();
            this.createTime = entity.getCreateTime();

            SysUserEntity cacheUser = CacheCenter.userCache.getObjectById(entity.getCreatorId());
            if (cacheUser != null) {
                this.creatorName = cacheUser.getUsername();
            }
        }
    }

    Date createTime;
    String creatorName;
    String noticeContent;
    String noticeId;
    String noticeTitle;
    Integer noticeType;
    Integer status;
}
