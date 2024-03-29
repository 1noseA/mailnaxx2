package com.mailnaxx2.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.SearchWeeklyReportForm;

@Mapper
public interface WeeklyReportsMapper {

    // 全件取得
    public List<WeeklyReports> findAll();

    // 所属メンバーの週報取得
    public List<WeeklyReports> findMyAffiliation(int affiliationId);

    // 自分の週報取得
    public List<WeeklyReports> findMine(int userId);

    // 検索
    public List<WeeklyReports> findBySearchForm(SearchWeeklyReportForm searchWeeklyReportForm);

    // 検索（一般権限）
    public List<WeeklyReports> findByMemberSearchForm(SearchWeeklyReportForm searchWeeklyReportForm);

    // 1件取得
    public WeeklyReports findById(int weeklyReportId);

    // 先週分取得
    public WeeklyReports findByLastWeek(int userId, LocalDate lastReportDate);

    // 1件排他ロック
    public WeeklyReports forLockById(int weeklyReportId);

    // 複数件排他ロック
    public List<WeeklyReports> forLockByIdList(List<Integer> idList);

    // 一括確認
    public void bulkConfirm(List<WeeklyReports> weeklyReportList);

    // 確認
    public void confirm(WeeklyReports weeklyReport);

    // コメント
    public void comment(WeeklyReports weeklyReport);

    // 共有
    public void share(WeeklyReports weeklyReport);

    // 既読
    public void readed(WeeklyReports weeklyReport);

    // 登録
    public int insert(WeeklyReports weeklyReport);

    // 更新
    public void update(WeeklyReports weeklyReport);

    // 一括物理削除
    public void bulkDelete(List<WeeklyReports> weeklyReportList);

    // 物理削除
    public void delete(int weeklyReportId);
}
