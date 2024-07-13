package com.mailnaxx2.form;

import jakarta.validation.constraints.NotBlank;

import com.mailnaxx2.validation.ValidGroup1;

import lombok.Data;

/**
 * 社員情報
 */
@Data
public class CategoriesForm {

    // カテゴリー名
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String categoryName;

    // 色
    private String color;

    // エラーメッセージ
    private String errorMessage;
}
