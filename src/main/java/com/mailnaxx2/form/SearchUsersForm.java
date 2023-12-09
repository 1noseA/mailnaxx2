package com.mailnaxx2.form;

import lombok.Data;

/**
 * 社員検索
 */
@Data
public class SearchUsersForm {

    // 氏名
    private String userName;

    // 検索条件
    private String searchCondition;

    // 権限区分
    private String roleClass;
}
