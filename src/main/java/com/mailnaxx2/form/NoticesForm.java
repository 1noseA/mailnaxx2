package com.mailnaxx2.form;

import jakarta.validation.constraints.NotBlank;

import com.mailnaxx2.validation.ValidGroup1;

import lombok.Data;

/**
 * 社員情報
 */
@Data
public class NoticesForm {

    // 掲載開始日_年
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String startYear;

    // 掲載開始日_月
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String startMonth;

    // 掲載開始日_日
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String startDay;

    // 掲載終了日_年
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String endYear;

    // 掲載終了日_月
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String endMonth;

    // 掲載終了日_日
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String endDay;

    // 表示範囲
    private String displayRange;

    // 内容
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String content;

    // リンク
    private String link;
}
