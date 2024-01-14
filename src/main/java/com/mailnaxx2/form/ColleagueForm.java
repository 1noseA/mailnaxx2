package com.mailnaxx2.form;

import org.hibernate.validator.constraints.Range;

import com.mailnaxx2.validation.ValidGroup1;
import com.mailnaxx2.validation.ValidGroup2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * 週報情報
 */
@Data
public class ColleagueForm {

	// 週報ID
    private int weeklyReportId;

    // 社員ID
	@Positive(groups = ValidGroup1.class, message = "選択してください")
    private int userId;

	// 現場社員名
	@Positive(groups = ValidGroup1.class, message = "選択してください")
    private String colleagueName;

    // 難易度
    @NotNull(groups = ValidGroup1.class, message = "入力してください")
    @Range(min = 0, max = 999, groups = ValidGroup2.class, message = "半角数字3桁で入力してください")
    private Integer colleagueDifficulty;

    // スケジュール感
    @NotNull(groups = ValidGroup1.class, message = "入力してください")
    @Range(min = 0, max = 999, groups = ValidGroup2.class, message = "半角数字3桁で入力してください")
    private Integer colleagueSchedule;

    // 状況
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String colleagueImpression;
}
