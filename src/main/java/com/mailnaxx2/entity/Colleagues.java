package com.mailnaxx2.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 現場社員状況
 */
@Data
public class Colleagues {

    // 現場社員状況ID
    private int colleagueId;

    // 週報ID
    private WeeklyReports weeklyReport;

    // 社員ID
    private Users user;

    // 難易度
    private int difficulty;

    // スケジュール感
    private int schedule;

    // 状況
    private String impression;

    // レコード登録者
    private String createdBy;

    // レコード登録日
    private LocalDateTime createdAt;

    // レコード更新者
    private String updatedBy;

    // レコード更新日
    private LocalDateTime updatedAt;
}
