package com.nexta1.domain.system.notice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nexta1.common.core.page.PageDTO;
import com.nexta1.domain.system.notice.DTO.NoticeDTO;
import com.nexta1.domain.system.notice.command.NoticeAddCommand;
import com.nexta1.domain.system.notice.model.NoticeModel;
import com.nexta1.domain.system.notice.model.NoticeModelFactory;
import com.nexta1.domain.system.notice.query.NoticeQuery;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import com.nexta1.orm.system.service.ISysNoticeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class NoticeApplicationService {

    @NonNull
    ISysNoticeService sysNotices;

    @NonNull
    NoticeModelFactory noticeModelFactory;

    public PageDTO<NoticeDTO> noticePage(NoticeQuery query) {
        Page<SysNoticeEntity> page = sysNotices.getNoticeList(query.toPage(), query.toQueryWrapper());
        List<NoticeDTO> records = page.getRecords().stream().map(NoticeDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public Integer addNotice(NoticeAddCommand command) {
        NoticeModel noticeModel = noticeModelFactory.create();
        noticeModel.loadAddCommand(command);
        noticeModel.checkFields();
        noticeModel.insert();
        return noticeModel.getNoticeId();
    }

}
