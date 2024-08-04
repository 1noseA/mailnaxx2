package com.mailnaxx2.form;

import lombok.Data;

@Data
public class DetailForm {

    // 週報ID
    private int weeklyReportId;

    // 社員ID
    private int userId;

    // コメント本文
    private String comment;
}
