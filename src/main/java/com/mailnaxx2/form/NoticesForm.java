package com.mailnaxx2.form;

import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;

import com.mailnaxx2.validation.ValidGroup1;

import lombok.Data;

/**
 * 社員情報
 */
@Data
public class NoticesForm {

    // 掲載開始日_年
    private String startYear;

    // 掲載開始日_月
    private String startMonth;

    // 掲載開始日_日
    private String startDay;

    // 掲載終了日_年
    private String endYear;

    // 掲載終了日_月
    private String endMonth;

    // 掲載終了日_日
    private String endDay;

    // 表示範囲
    private String displayRange;

    // 社員ID（表示範囲：個人）
    private HashSet<Integer> userId;

    // カテゴリー
    private int categoryId;

    // タイトル
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String title;

    // 内容
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String content;

    // リンク
    private String link;
}
