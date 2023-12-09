package com.mailnaxx2.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * ユーザ
 */
@Data
public class Users {

    // ユーザID
    private int userId;

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

    // パスワード変更日時
    private LocalDateTime passChangedDate;

    // 前回パスワード
    private String oldPassword;

    // ログイン失敗回数
    private int failureCount;

    // 最終ログイン日時
    private LocalDateTime lastLoginDate;

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
