package com.mailnaxx2.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mailnaxx2.entity.Projects;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.DetailForm;
import com.mailnaxx2.form.SearchWeeklyReportForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.form.WeeklyReportForm;
import com.mailnaxx2.mapper.ColleaguesMapper;
import com.mailnaxx2.mapper.WeeklyReportsMapper;
import com.mailnaxx2.security.LoginUserDetails;

@Service
public class WeeklyReportsService {

    @Autowired
    WeeklyReportsMapper weeklyReportsMapper;

    @Autowired
    ColleaguesMapper colleaguesMapper;

    // 週報一覧
    List<WeeklyReports> weeklyReportList;

    // 週報詳細
    WeeklyReports weeklyReportInfo;

    // 全件取得
    public List<WeeklyReports> findAll() {
        weeklyReportList = weeklyReportsMapper.findAll();
        return weeklyReportList;
    }

    // 所属メンバーの週報取得
    public List<WeeklyReports> findMyAffiliation(int affiliationId) {
        weeklyReportList = weeklyReportsMapper.findMyAffiliation(affiliationId);
        return weeklyReportList;
    }

    // 自分の週報取得
    public List<WeeklyReports> findMine(int userId) {
        weeklyReportList = weeklyReportsMapper.findMine(userId);
        return weeklyReportList;
    }

    // 検索結果取得
    public List<WeeklyReports> findBySearchForm(SearchWeeklyReportForm searchWeeklyReportForm) {
        weeklyReportList = weeklyReportsMapper.findBySearchForm(searchWeeklyReportForm);
        return weeklyReportList;
    }

    // 検索結果取得（一般権限）
    public List<WeeklyReports> findByMemberSearchForm(SearchWeeklyReportForm searchWeeklyReportForm) {
        weeklyReportList = weeklyReportsMapper.findByMemberSearchForm(searchWeeklyReportForm);
        return weeklyReportList;
    }

    // 一括確認処理
    @Transactional
    public void bulkConfirm(SelectForm selectForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < selectForm.getSelectId().size(); i++) {
            idList.add(selectForm.getSelectId().get(i));
        }
        // 複数件排他ロック
        weeklyReportList = weeklyReportsMapper.forLockByIdList(idList);

        for (int i = 0; i < weeklyReportList.size(); i++) {
            // 更新者はセッションの社員番号
            weeklyReportList.get(i).setUpdatedBy(loginUser.getLoginUser().getUserNumber());
        }

        // 一括確認
        weeklyReportsMapper.bulkConfirm(weeklyReportList);
    }

    // 詳細情報取得
    public WeeklyReports findById(int weeklyReportId) {
        weeklyReportInfo = weeklyReportsMapper.findById(weeklyReportId);
        return weeklyReportInfo;
    }

    // 先週分取得
    public WeeklyReports findByLastWeek(int userId, LocalDate lastReportDate) {
        WeeklyReports lastWeekReportInfo = weeklyReportsMapper.findByLastWeek(userId, lastReportDate);
        return lastWeekReportInfo;
    }

    // 確認処理
    @Transactional
    public void confirm(int weeklyReportId, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 排他ロック
        WeeklyReports weeklyReport = weeklyReportsMapper.forLockById(weeklyReportId);

        // 更新者はセッションの社員番号
        weeklyReport.setUpdatedBy(loginUser.getLoginUser().getUserNumber());

        // 確認
        weeklyReportsMapper.confirm(weeklyReport);
    }

    // コメント処理
    @Transactional
    public void comment(DetailForm detailForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 排他ロック
        WeeklyReports weeklyReport = weeklyReportsMapper.forLockById(detailForm.getWeeklyReportId());

        // 本文
        weeklyReport.setComment(detailForm.getComment());
        // 更新者はセッションの社員番号
        weeklyReport.setUpdatedBy(loginUser.getLoginUser().getUserNumber());

        // コメント
        weeklyReportsMapper.comment(weeklyReport);
    }

    // 共有処理
    @Transactional
    public void share(int weeklyReportId, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 排他ロック
        WeeklyReports weeklyReport = weeklyReportsMapper.forLockById(weeklyReportId);

        // 更新者はセッションの社員番号
        weeklyReport.setUpdatedBy(loginUser.getLoginUser().getUserNumber());

        // 共有
        weeklyReportsMapper.share(weeklyReport);
    }

    // 既読処理
    @Transactional
    public void readed(int weeklyReportId, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 排他ロック
        WeeklyReports weeklyReport = weeklyReportsMapper.forLockById(weeklyReportId);

        // 更新者はセッションの社員番号
        weeklyReport.setUpdatedBy(loginUser.getLoginUser().getUserNumber());

        // 既読
        weeklyReportsMapper.readed(weeklyReport);
    }

    // 登録処理
    @Transactional
    public void insert(WeeklyReportForm weeklyReportForm,
                       @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力値をセットする
        WeeklyReports weeklyReport = setWeeklyReport(new WeeklyReports(), weeklyReportForm);

        // 社員ID
        Users user = loginUser.getLoginUser();
        user.setUserId(loginUser.getLoginUser().getUserId());
        weeklyReport.setUser(user);

        // 作成者はセッションの社員番号
        weeklyReport.setCreatedBy(loginUser.getLoginUser().getUserNumber());

        // 週報登録
        weeklyReportsMapper.insert(weeklyReport);

        // 現場社員が存在する場合
        if (weeklyReportForm.getColleagueId() != 0) {
            // 登録した週報IDを取得
            int weeklyReportId = weeklyReport.getWeeklyReportId();
            // 現場社員に週報IDを追加
            colleaguesMapper.addWeeklyReportId(weeklyReportForm.getColleagueId(), weeklyReportId);
        }
    }

    // 更新処理
    @Transactional
    public void update(WeeklyReportForm weeklyReportForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 排他ロック
        WeeklyReports weeklyReport = weeklyReportsMapper.forLockById(weeklyReportForm.getWeeklyReportId());

        // 入力値をセットする
        weeklyReport = setWeeklyReport(weeklyReport, weeklyReportForm);

        // 更新者はセッションの社員番号
        weeklyReport.setUpdatedBy(loginUser.getLoginUser().getUserNumber());

        // 更新
        weeklyReportsMapper.update(weeklyReport);
    }

    // メール送信処理

    // 入力値をセットする
    private WeeklyReports setWeeklyReport(WeeklyReports weeklyReport, WeeklyReportForm weeklyReportForm) {
        // 現場ID
        Projects project = new Projects();
        project.setProjectId(weeklyReportForm.getProjectId());
        Users salesUser = new Users();
        salesUser.setUserId(weeklyReportForm.getSalesUserId());
        project.setSalesUser(salesUser);
        weeklyReport.setProject(project);

        // 報告対象週
        weeklyReport.setReportDate(weeklyReportForm.getReportDate());

        // 平均残業時間
        weeklyReport.setAveOvertimeHours(weeklyReportForm.getAveOvertimeHours());

        // 進捗状況
        weeklyReport.setProgress(weeklyReportForm.getProgress());

        // 体調
        weeklyReport.setCondition(weeklyReportForm.getCondition());

        // 人間関係
        weeklyReport.setRelationship(weeklyReportForm.getRelationship());

        // 今週の計画
        weeklyReport.setPlan(weeklyReportForm.getPlan());

        // 作業内容
        weeklyReport.setWorkContent(weeklyReportForm.getWorkContent());

        // 難易度
        weeklyReport.setDifficulty(weeklyReportForm.getDifficulty());

        // スケジュール感
        weeklyReport.setSchedule(weeklyReportForm.getSchedule());

        // 結果
        weeklyReport.setResult(weeklyReportForm.getResult());

        // 所感
        weeklyReport.setImpression(weeklyReportForm.getImpression());

        // 改善点
        weeklyReport.setImprovements(weeklyReportForm.getImprovements());

        // 次週の計画
        weeklyReport.setNextPlan(weeklyReportForm.getNextPlan());

        // 特記事項
        weeklyReport.setRemarks(weeklyReportForm.getRemarks());

        // ステータス
        weeklyReport.setStatus(weeklyReportForm.getStatus());

        return weeklyReport;
    }

    // 一括物理削除処理
    @Transactional
    public void bulkDelete(SelectForm selectForm) {
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < selectForm.getSelectUserId().size(); i++) {
            idList.add(selectForm.getSelectUserId().get(i));
        }
        // 複数件排他ロック
        weeklyReportList = weeklyReportsMapper.forLockByIdList(idList);

        // 一括物理削除
        weeklyReportsMapper.bulkDelete(weeklyReportList);
    }

    // 物理削除処理
    @Transactional
    public void delete(int weeklyReportId) {
        // 物理削除
        weeklyReportsMapper.delete(weeklyReportId);
    }
}
