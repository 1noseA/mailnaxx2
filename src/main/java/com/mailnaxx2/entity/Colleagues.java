package com.mailnaxx2.entity;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * 現場社員状況
 */
@Getter
public class Colleagues {

    // 現場社員状況ID
    private int colleague_id;

    // 週報ID
    private int weekly_report_id;

    // ユーザID
    private int user_id;

    // 難易度
    private int difficulty;

    // スケジュール感
    private int schedule;

    // 状況
    private String impression;

    // レコード登録者
    private String created_by;

    // レコード登録日
    private LocalDateTime created_at;

    // レコード更新者
    private String updated_by;

    // レコード更新日
    private LocalDateTime updated_at;
}
