package com.mailnaxx2.values;

// 処理区分
public enum ProcessClass {

    INSERT("1", "登録"),
    UPDATE("2", "更新");

    // コード値
    private final String code;
    // 表示名
    private final String viewName;

    private ProcessClass(String code, String viewName) {
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
    public static ProcessClass getValueByCode(String code) {
        for (ProcessClass value : ProcessClass.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    // コード値から表示名を取得
    public static String getViewNameByCode(String code) {
        for (ProcessClass value : ProcessClass.values()) {
            if (value.getCode().equals(code)) {
                return value.getViewName();
            }
        }
        return null;
    }
}
