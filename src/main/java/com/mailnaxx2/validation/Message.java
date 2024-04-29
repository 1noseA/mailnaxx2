package com.mailnaxx2.validation;

import lombok.Data;

/**
 * 社員情報一括登録_エラーメッセージ
 */
@Data
public class Message {

    // 行数
    private String lineNum;

    // 項目名
    private String item;

    // メッセージ
    private String content;
}
