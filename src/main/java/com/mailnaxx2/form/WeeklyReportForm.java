package com.mailnaxx2.form;

import java.time.LocalDate;

import com.mailnaxx2.validation.ValidGroup1;
import com.mailnaxx2.validation.ValidGroup2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * 週報情報
 */
@Data
public class WeeklyReportForm {

    // 担当営業
	@Positive(groups = ValidGroup1.class, message = "選択してください")
    private int salesUserId;

    // 現場ID
	@Positive(groups = ValidGroup1.class, message = "選択してください")
    private int projectId;

    // 報告対象週
    private LocalDate reportDate;

    // 平均残業時間
    @PositiveOrZero(groups = ValidGroup1.class, message = "半角数字で入力してください")
    private int aveOvertimeHours;

    // 進捗状況
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String progress;

    // 体調
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String condition;

    // 人間関係
    @NotBlank(groups = ValidGroup1.class, message = "選択してください")
    private String relationship;

    // 今週の計画
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String plan;

    // 作業内容
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String workContent;

    // 難易度
    @PositiveOrZero(groups = ValidGroup1.class, message = "半角数字3桁で入力してください")
    @Pattern(regexp="^[0-9]+$", groups = ValidGroup2.class, message = "半角数字3桁で入力してください")
    private int difficulty;

    // スケジュール感
    @PositiveOrZero(groups = ValidGroup1.class, message = "半角数字3桁で入力してください")
    @Pattern(regexp="^[0-9]+$", groups = ValidGroup2.class, message = "半角数字3桁で入力してください")
    private int schedule;

    // 結果
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String result;

    // 所感
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String impression;

    // 改善点
    private String improvements;

    // 次週の計画
    @NotBlank(groups = ValidGroup1.class, message = "入力してください")
    private String nextPlan;

    // 特記事項
    private String remarks;
}
