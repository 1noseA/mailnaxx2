package com.mailnaxx2.dto;

import java.time.LocalDate;

import com.mailnaxx2.entity.Affiliations;

import lombok.Data;

/**
 * 社員情報一括登録DTO
 */
@Data
public class BulkRegistUsersDTO {

    // 処理区分
    private String processClass;

    // 社員番号
    private String userNumber;

    // 氏名_漢字
    private String userName;

    // 氏名_カナ
    private String userNameKana;

    // 入社年月
    private LocalDate hireDate;

    // 所属
    private Affiliations affiliation;

    // 権限区分
    private String roleClass;

    // 営業フラグ
    private String salesFlg;

    // 生年月日
    private LocalDate birthDate;

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
