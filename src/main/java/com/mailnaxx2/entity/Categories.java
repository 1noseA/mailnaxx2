package com.mailnaxx2.entity;

import java.time.LocalDateTime;

import lombok.Getter;

/**
 * お知らせ
 */
@Getter
public class Categories {

    // カテゴリーID
    private int categoryId;

    // カテゴリー名
    private String categoryName;

    // 色
    private String color;

    // レコード登録者
    private String createdBy;

    // レコード登録日
    private LocalDateTime createdAt;

    // レコード更新者
    private String updatedBy;

    // レコード更新日
    private LocalDateTime updatedAt;
}
