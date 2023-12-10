package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.SearchWeeklyReportForm;

@Mapper
public interface WeeklyReportsMapper {

    // 全件取得
    public List<WeeklyReports> findAll();

    // 検索
    public List<WeeklyReports> findBySearchForm(SearchWeeklyReportForm searchWeeklyReportForm);

    // 1件取得
    public WeeklyReports findById(int weeklyReportId);

    // 1件排他ロック
    public WeeklyReports forLockById(int weeklyReportId);

    // 複数件排他ロック
    public List<WeeklyReports> forLockByIdList(List<Integer> idList);

    // 確認
    public void confirm(WeeklyReports weeklyReports);

    // 一括確認
    public void bulkConfirm(List<WeeklyReports> weeklyReportList);

    // 登録
    public void insert(WeeklyReports weeklyReports);
}
