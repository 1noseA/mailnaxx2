package com.mailnaxx2.form;

import lombok.Data;

/**
 * 週報検索
 */
@Data
public class SearchWeeklyReportForm {

    // 所属
    private int affiliationId;

    // 担当営業
    private int salesUserId;

    // 氏名
    private String userName;

    // 報告対象週（From）
    private String fromReportDate;

    // 報告対象週（To）
    private String toReportDate;

    // 確認済みフラグ
    private String confirmedFlg;
}
