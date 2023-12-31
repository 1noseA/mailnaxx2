package com.mailnaxx2.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 週報
 */
@Data
public class WeeklyReports {

    // 週報ID
    private int weeklyReportId;

    // ユーザID
    private Users user;

    // 現場ID
    private Projects project;

    // 報告対象週
    private LocalDate reportDate;

    // 平均残業時間
    private double aveOvertimeHours;

    // 進捗状況
    private String progress;

    // 体調
    private String condition;

    // 人間関係
    private String relationship;

    // 今週の計画
    private String plan;

    // 作業内容
    private String workContent;

    // 難易度
    private int difficulty;

    // スケジュール感
    private int schedule;

    // 結果
    private String result;

    // 所感
    private String impression;

    // 改善点
    private String improvements;

    // 次週の計画
    private String nextPlan;

    // 特記事項
    private String remarks;

    // ステータス
    private String status;

    // コメント
    private String comment;

    // 既読フラグ
    private String readedFlg;

    // 共有フラグ
    private String sharedFlg;

    // レコード登録者
    private String createdBy;

    // レコード登録日
    private LocalDateTime createdAt;

    // レコード更新者
    private String updatedBy;

    // レコード更新日
    private LocalDateTime updatedAt;
}
