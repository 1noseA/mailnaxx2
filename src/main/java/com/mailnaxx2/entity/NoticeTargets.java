package com.mailnaxx2.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * お知らせ表示対象
 */
@Data
public class NoticeTargets {

    // お知らせ表示対象ID
    private int noticeTargetId;

    // お知らせID
    private int noticeId;

    // 社員ID
    private int userId;

    // 既読フラグ
    private String displayRange;

    // レコード登録者
    private String createdBy;

    // レコード登録日
    private LocalDateTime createdAt;

    // レコード更新者
    private String updatedBy;

    // レコード更新日
    private LocalDateTime updatedAt;
}
