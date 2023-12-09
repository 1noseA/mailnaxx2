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
    private int notice_id;

    // 掲載開始日
    private LocalDate start_date;

    // 掲載終了日
    private LocalDate end_date;

    // 表示範囲
    private String display_range;

    // ユーザID
    private int user_id;

    // 内容
    private String notice_message;

    // リンク
    private String reference_link;

    // レコード登録者
    private String created_by;

    // レコード登録日
    private LocalDateTime created_at;

    // レコード更新者
    private String updated_by;

    // レコード更新日
    private LocalDateTime updated_at;
}
