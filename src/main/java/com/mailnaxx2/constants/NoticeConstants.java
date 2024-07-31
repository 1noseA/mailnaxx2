package com.mailnaxx2.constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class NoticeConstants {

    // お知らせ表示範囲（全体）
    public static final String DISPLAY_RANGE_ALL = "1";

    // お知らせ表示範囲（個人）
    public static final String DISPLAY_RANGE_INDIVIDUAL = "2";

    // ラジオボタン
    @SuppressWarnings("serial")
    public static final Map<String, String> RADIO = new LinkedHashMap<>() {{
        put("1", "全体");
        put("2", "個人");
    }};

    // カテゴリーデフォルトカラー
    public static final String DEFAULT_COLOR = "#6c757d";

    // 既読フラグ（未読）
    public static final String UNREAD = "0";

    // 既読フラグ（既読）
    public static final String READED = "1";
}
