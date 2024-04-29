package com.mailnaxx2.values;

// 処理区分
public enum BulkRegistCsvItem {

    ITEM1("0", "処理区分"),
    ITEM2("1", "社員番号"),
    ITEM3("2", "氏名(漢字)_姓"),
    ITEM4("3", "氏名(漢字)_名"),
    ITEM5("4", "氏名(カタカナ)_姓"),
    ITEM6("5", "氏名(カタカナ)_名"),
    ITEM7("6", "入社年月_年"),
    ITEM8("7", "入社年月_月"),
    ITEM9("8", "所属"),
    ITEM10("9", "権限区分"),
    ITEM11("10", "営業"),
    ITEM12("11", "生年月日_年"),
    ITEM13("12", "生年月日_月"),
    ITEM14("13", "生年月日_日"),
    ITEM15("14", "郵便番号1"),
    ITEM16("15", "郵便番号2"),
    ITEM17("16", "住所"),
    ITEM18("17", "電話番号1"),
    ITEM19("18", "電話番号2"),
    ITEM20("19", "電話番号3"),
    ITEM21("20", "メールアドレス"),
    ITEM22("21", "パスワード");

    // コード値
    private final String code;
    // 表示名
    private final String viewName;

    private BulkRegistCsvItem(String code, String viewName) {
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
    public static BulkRegistCsvItem getValueByCode(String code) {
        for (BulkRegistCsvItem value : BulkRegistCsvItem.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    // コード値から表示名を取得
    public static String getViewNameByCode(String code) {
        for (BulkRegistCsvItem value : BulkRegistCsvItem.values()) {
            if (value.getCode().equals(code)) {
                return value.getViewName();
            }
        }
        return null;
    }
}
