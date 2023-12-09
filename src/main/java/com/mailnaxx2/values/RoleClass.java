package com.mailnaxx2.values;

// ユーザ.権限区分
public enum RoleClass {

    MEMBER("1", "一般"),
    LEADER("2", "リーダ・チーフ"),
    MANAGER("3", "マネジャー"),
    AFFAIRS("4", "総務"),
    PRESIDENT("5", "社長");

    // コード値
    private final String code;
    // 表示名
    private final String viewName;

    private RoleClass(String code, String viewName) {
        this.code = code;
        this.viewName = viewName;
    }

    public String getCode() {
        return this.code;
    }

    public String getViewName() {
        return this.viewName;
    }

    // コード値からEnum定数を取得
    public static RoleClass getValueByCode(String code) {
        for (RoleClass value : RoleClass.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    // コード値から表示名を取得
    public static String getViewNameByCode(String code) {
        for (RoleClass value : RoleClass.values()) {
            if (value.getCode().equals(code)) {
                return value.getViewName();
            }
        }
        return null;
    }
}
