package com.mailnaxx2.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 選択対象
 */
@Data
public class SelectForm {

    // 選択チェックボックス(社員ID)
    private List<Integer> selectUserId;

    // 選択チェックボックス(週報ID)
    private List<Integer> selectWeeklyReportId;

    // 選択チェックボックス(マニュアルID, 社員ID)
    private Map<String, String> selectManual = new HashMap<String, String>();
}
