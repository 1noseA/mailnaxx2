package com.mailnaxx2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mailnaxx2.entity.Projects;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.SearchWeeklyReportForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.form.WeeklyReportForm;
import com.mailnaxx2.mapper.WeeklyReportsMapper;
import com.mailnaxx2.security.LoginUserDetails;

@Service
public class WeeklyReportsService {

    @Autowired
    WeeklyReportsMapper weeklyReportsMapper;

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

    // 一括確認処理
    @Transactional
    public void bulkConfirm(SelectForm selectForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < selectForm.getSelectWeeklyReportId().size(); i++) {
            idList.add(selectForm.getSelectWeeklyReportId().get(i));
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

    // 登録処理
    @Transactional
    public void insert(WeeklyReportForm weeklyReportForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力値をセットする
        WeeklyReports weeklyReport = setWeeklyReportForm(new WeeklyReports(), weeklyReportForm);

        // ユーザID
        Users user = loginUser.getLoginUser();
        user.setUserId(loginUser.getLoginUser().getUserId());
        weeklyReport.setUser(user);

        // 作成者はセッションの社員番号
        weeklyReport.setCreatedBy(loginUser.getLoginUser().getUserNumber());

        // 登録
        weeklyReportsMapper.insert(weeklyReport);
    }

    // 更新処理
    @Transactional
    public void update(WeeklyReports weeklyReport, WeeklyReportForm weeklyReportForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 排他ロック
    	weeklyReport = weeklyReportsMapper.forLockById(weeklyReport.getWeeklyReportId());

        // 入力値をセットする
    	weeklyReport = setWeeklyReportForm(weeklyReport, weeklyReportForm);

        // 更新者はセッションの社員番号
    	weeklyReport.setUpdatedBy(loginUser.getLoginUser().getUserNumber());

        // 更新
        weeklyReportsMapper.update(weeklyReport);
    }

    // メール送信処理

    // 入力値をセットする
    private WeeklyReports setWeeklyReportForm(WeeklyReports weeklyReport, WeeklyReportForm weeklyReportForm) {
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
    	weeklyReport.setAveOvertimeHours(Integer.parseInt(weeklyReportForm.getAveOvertimeHours()));

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
    	weeklyReport.setDifficulty(Integer.parseInt(weeklyReportForm.getDifficulty()));

    	// スケジュール感
    	weeklyReport.setSchedule(Integer.parseInt(weeklyReportForm.getSchedule()));

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

        return weeklyReport;
    }

    // 物理削除処理
    @Transactional
    public void delete(SelectForm selectForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < selectForm.getSelectUserId().size(); i++) {
            idList.add(selectForm.getSelectUserId().get(i));
        }
        // 複数件排他ロック
        weeklyReportList = weeklyReportsMapper.forLockByIdList(idList);

        // 物理削除
        weeklyReportsMapper.delete(weeklyReportList);
    }
}
