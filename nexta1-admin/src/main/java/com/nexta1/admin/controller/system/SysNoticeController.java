package com.nexta1.admin.controller.system;

import com.nexta1.common.core.dto.ResponseDTO;
import com.nexta1.common.core.page.PageDTO;
import com.nexta1.core.annotations.AccessLog;
import com.nexta1.domain.system.notice.DTO.NoticeDTO;
import com.nexta1.domain.system.notice.NoticeApplicationService;
import com.nexta1.domain.system.notice.command.NoticeAddCommand;
import com.nexta1.domain.system.notice.query.NoticeQuery;
import com.nexta1.orm.common.enums.BusinessTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author nexta1
 * @date 2023/5/16 20:39
 */
@Tag(name = "公告类")
@RestController("/system/notice")
@RequiredArgsConstructor
public class SysNoticeController {

    @NonNull
    NoticeApplicationService noticeApplicationService;

    @Operation(summary = "公告列表")
    @PreAuthorize("@permission.has('system:notice:list')")
    @GetMapping("/list")
    public ResponseDTO<PageDTO<NoticeDTO>> list(NoticeQuery query) {
        return ResponseDTO.ok(noticeApplicationService.noticePage(query));
    }

    @Operation(summary = "新增公告")
    @PreAuthorize("@permission.has('system:notice:add')")
    @AccessLog(title = "参数管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping
    public ResponseDTO<Integer> add(@RequestBody NoticeAddCommand addCommand) {
        return ResponseDTO.ok(noticeApplicationService.addNotice(addCommand));
    }
}
