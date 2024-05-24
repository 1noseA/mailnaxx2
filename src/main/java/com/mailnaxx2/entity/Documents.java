package com.mailnaxx2.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 資料
 */
@Data
public class Documents {

    // 資料ID
    private int documentId;

    // ファイル名
    private String fileName;

    // 表示名
    private int displayName;

    // ファイルデータ
    private String fileData;

    // 削除フラグ
    private String deletedFlg;

    // レコード登録者
    private String createdBy;

    // レコード登録日
    private LocalDateTime createdAt;

    // レコード更新者
    private String updatedBy;

    // レコード更新日
    private LocalDateTime updatedAt;
}
