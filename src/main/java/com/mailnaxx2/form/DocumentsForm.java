package com.mailnaxx2.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 資料
 */
@Data
public class DocumentsForm {

    // 表示名
    private String displayName;

    // ファイルデータ
    private MultipartFile fileData;
}