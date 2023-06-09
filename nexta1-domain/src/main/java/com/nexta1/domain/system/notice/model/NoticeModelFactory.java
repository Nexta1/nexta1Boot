package com.nexta1.domain.system.notice.model;


import com.nexta1.common.exception.ApiException;
import com.nexta1.common.exception.error.ErrorCode;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import com.nexta1.orm.system.service.ISysNoticeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 公告模型工厂
 *
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class NoticeModelFactory {

    @NonNull
    private ISysNoticeService noticeService;

    public NoticeModel loadById(Long noticeId) {
        SysNoticeEntity byId = noticeService.getById(noticeId);

        if (byId == null) {
            throw new ApiException(ErrorCode.Business.OBJECT_NOT_FOUND, noticeId, "通知公告");
        }

        return new NoticeModel(byId);
    }

    public NoticeModel create() {
        return new NoticeModel();
    }


}
