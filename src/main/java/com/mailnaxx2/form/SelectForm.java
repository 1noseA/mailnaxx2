package com.mailnaxx2.form;

import java.util.List;

import lombok.Data;

/**
 * 選択対象
 */
@Data
public class SelectForm {

    // 選択チェックボックス
    private List<Integer> selectTarget;
}
