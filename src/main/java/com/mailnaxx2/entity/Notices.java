package com.mailnaxx2.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

/**
 * お知らせ
 */
@Getter
public class Notices {

    // お知らせID
    private int noticeId;

    // 掲載開始日
    private LocalDate startDate;

    // 掲載終了日
    private LocalDate endDate;

    // 表示範囲
    private String displayRange;

    // ユーザID
    private int userId;

    // 内容
    private String noticeMessage;

    // リンク
    private String referenceLink;

    // レコード登録者
    private String createdBy;

    // レコード登録日
    private LocalDateTime createdAt;

    // レコード更新者
    private String updatedBy;

    // レコード更新日
    private LocalDateTime updatedAt;
}
