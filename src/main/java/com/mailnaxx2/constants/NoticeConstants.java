package com.mailnaxx2.constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class NoticeConstants {

    // お知らせ表示範囲（全体）
    public static final String DISPLAY_RANGE_ALL = "1";

    // ラジオボタン
    @SuppressWarnings("serial")
    public static final Map<String, String> RADIO = new LinkedHashMap<>() {{
        put("1", "全体");
        put("2", "個人");
    }};
}
