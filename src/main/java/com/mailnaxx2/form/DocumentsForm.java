package com.mailnaxx2.form;

import lombok.Data;

/**
 * 資料
 */
@Data
public class DocumentsForm {

    // ファイル名
    private String fileName;

    // 表示名
    private String displayName;

    // ファイルデータ
    private String fileData;
}
