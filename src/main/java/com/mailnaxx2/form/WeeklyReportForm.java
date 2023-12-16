package com.mailnaxx2.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WeeklyReportForm {

    // 担当営業
    private int salesUserId;

    // 現場ID
    private int projectId;

    // 報告対象週
    private LocalDate reportDate;

    // 平均残業時間
    private int aveOvertimeHours;

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

}
