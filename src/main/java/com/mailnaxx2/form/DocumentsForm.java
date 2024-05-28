package com.mailnaxx2.form;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 資料
 */
@Data
public class DocumentsForm {

    // 表示名
    private String displayName;

    // 表示順
    @Range(min = 1, max = 999, message = "半角数字3桁で入力してください")
    private Integer displayOrder;

    // ファイルデータ
    private MultipartFile fileData;
}
