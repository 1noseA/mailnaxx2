package com.mailnaxx2.model;

import lombok.Data;

/**
 * 社員情報CSV
 */
@Data
public class UsersCsv {

    // 処理区分
    private String processClass;

    // 社員番号
    private String userNumber;

    // 氏名_漢字
    private String userName;

    // 氏名_カナ
    private String userNameKana;

    // 入社年月
    private String hireDate;

    // 所属
    private String affiliationId;

    // 権限区分
    private String roleClass;

    // 営業フラグ
    private String salesFlg;

    // 生年月日
    private String birthDate;

    // 郵便番号
    private String postCode;

    // 住所
    private String address;

    // 電話番号
    private String phoneNumber;

    // メールアドレス
    private String emailAddress;

    // パスワード
    private String password;
}
