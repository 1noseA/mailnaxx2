package com.mailnaxx2.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 資料
 */
@Data
public class DocumentsForm {

    // 表示名
    private String displayName;

    // 表示順
    @Pattern(regexp = "^$|^[0-9]{1,3}$", message = "半角数字3桁で入力してください")
    private String displayOrder;

    // ファイルデータ
    private MultipartFile fileData;
}
