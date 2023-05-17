package com.nexta1.domain.system.notice.model;

import cn.hutool.core.bean.BeanUtil;
import com.nexta1.core.util.BasicEnumUtil;
import com.nexta1.domain.system.notice.command.NoticeAddCommand;
import com.nexta1.orm.common.enums.NoticeTypeEnum;
import com.nexta1.orm.common.enums.StatusEnum;
import com.nexta1.orm.system.entity.SysNoticeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author nexta1
 * @date 2023/5/17 23:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class NoticeModel extends SysNoticeEntity {

    public NoticeModel(SysNoticeEntity entity) {
        if (entity != null) {
            BeanUtil.copyProperties(entity, this);
        }
    }

    public void loadAddCommand(NoticeAddCommand command) {
        if (command != null) {
            BeanUtil.copyProperties(command, this, "noticeId");
        }
    }

//    public void loadUpdateCommand(NoticeUpdateCommand command) {
//        if (command != null) {
//            loadAddCommand(command);
//        }
//    }

    public void checkFields() {
        BasicEnumUtil.fromValue(NoticeTypeEnum.class, getNoticeType());
        BasicEnumUtil.fromValue(StatusEnum.class, getStatus());
    }

}
