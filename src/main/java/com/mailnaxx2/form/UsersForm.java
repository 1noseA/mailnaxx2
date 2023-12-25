package com.mailnaxx2.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.mailnaxx2.validation.CreateUser;
import com.mailnaxx2.validation.ValidGroup1;
import com.mailnaxx2.validation.ValidGroup2;
import com.mailnaxx2.validation.ValidGroup3;

import lombok.Data;

/**
 * 社員情報
 */
@Data
public class UsersForm {

    // 氏名(漢字)_姓
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Size(max=10, groups = ValidGroup2.class, message = "全角10文字以内で入力してください")
    @Pattern(regexp="^[^ -~｡-ﾟ]+$", groups = ValidGroup3.class, message = "全角文字で入力してください")
    private String userLastName;

    // 氏名(漢字)_名
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Size(max=10, groups = ValidGroup2.class, message = "全角10文字以内で入力してください")
    @Pattern(regexp="^[^ -~｡-ﾟ]+$", groups = ValidGroup2.class, message = "全角文字で入力してください")
    private String userFirstName;

    // 氏名(カタカナ)_姓
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Size(max=10, groups = ValidGroup2.class, message = "全角10文字以内で入力してください")
    @Pattern(regexp="^[ァ-ー]+$", groups = ValidGroup2.class, message = "全角カナで入力してください")
    private String userLastKana;

    // 氏名(カタカナ)_名
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Size(max=10, groups = ValidGroup2.class, message = "全角10文字以内で入力してください")
    @Pattern(regexp="^[ァ-ー]+$", groups = ValidGroup2.class, message = "全角カナで入力してください")
    private String userFirstKana;

    // 入社年月_年
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String hireYear;

    // 入社年月_月
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String hireMonth;

    // 所属
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String affiliationId;

    // 権限区分
    private String roleClass;

    // 営業担当
    private String salesFlg;

    // 生年月日_年
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String birthYear;

    // 生年月日_月
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String birthMonth;

    // 生年月日_日
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String birthDay;

    // 郵便番号1
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Pattern(regexp="^[0-9]+$", groups = ValidGroup2.class, message = "半角数字3桁で入力してください")
    private String postCode1;

    // 郵便番号2
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Pattern(regexp="^[0-9]+$", groups = ValidGroup2.class, message = "半角数字4桁で入力してください")
    private String postCode2;

    // 住所
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Size(max=256, groups = ValidGroup2.class, message = "全角256文字以内で入力してください")
    @Pattern(regexp="^[^ -~｡-ﾟ]+$", groups = ValidGroup2.class, message = "全角文字で入力してください")
    private String address;

    // 電話番号1
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Pattern(regexp="^[0-9]+$", groups = ValidGroup2.class, message = "半角数字5桁以内で入力してください")
    private String phoneNumber1;

    // 電話番号2
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Pattern(regexp="^[0-9]+$", groups = ValidGroup2.class, message = "半角数字4桁以内で入力してください")
    private String phoneNumber2;

    // 電話番号3
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Pattern(regexp="^[0-9]+$", groups = ValidGroup2.class, message = "半角数字4桁以内で入力してください")
    private String phoneNumber3;

    // メールアドレス
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    @Size(max=128, groups = ValidGroup2.class, message = "半角128文字以内で入力してください")
    @Email(groups = ValidGroup3.class, message = "メールアドレスの形式が間違っています")
    private String emailAddress;

    // パスワード
    @NotBlank(groups = CreateUser.class, message = "入力してください")
    @Pattern(regexp="(^$|.{8,10})", groups = ValidGroup2.class, message = "半角英数字8文字以上10文字以内で入力してください")
    @Pattern(regexp="(^$|^[A-Za-z0-9]+$)", groups = ValidGroup3.class, message = "半角英数字で入力してください")
    private String password;
}
