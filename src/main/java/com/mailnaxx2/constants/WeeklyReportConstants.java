package com.mailnaxx2.constants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeeklyReportConstants {

    // ステータス
    @SuppressWarnings("serial")
	public static final Map<String, String> STATUS = new HashMap<String, String>() {{
        put("1", "一時保存");
        put("2", "提出済み");
        put("3", "確認済み");
    }};

    // ラジオボタン
    @SuppressWarnings("serial")
	public static final Map<String, String> RADIO = new LinkedHashMap<>() {{
    	put("1", "良い");
        put("2", "やや良い");
        put("3", "普通");
        put("4", "やや悪い");
        put("5", "悪い");
    }};
}
