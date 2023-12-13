package com.mailnaxx2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.SearchWeeklyReportForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.mapper.WeeklyReportsMapper;
import com.mailnaxx2.security.LoginUserDetails;

@Service
public class WeeklyReportsService {

    @Autowired
    WeeklyReportsMapper weeklyReportsMapper;

    // 全件取得
    public List<WeeklyReports> findAll() {
        List<WeeklyReports> weeklyReportList = weeklyReportsMapper.findAll();
        return weeklyReportList;
    }

    // 所属メンバーの週報取得
    public List<WeeklyReports> findMyAffiliation(int affiliationId) {
        List<WeeklyReports> weeklyReportList = weeklyReportsMapper.findMyAffiliation(affiliationId);
        return weeklyReportList;
    }

    // 自分の週報取得
    public List<WeeklyReports> findMine(int userId) {
        List<WeeklyReports> weeklyReportList = weeklyReportsMapper.findMine(userId);
        return weeklyReportList;
    }

    // 検索結果取得
    public List<WeeklyReports> findBySearchForm(SearchWeeklyReportForm searchWeeklyReportForm) {
        List<WeeklyReports> weeklyReportList = weeklyReportsMapper.findBySearchForm(searchWeeklyReportForm);
        return weeklyReportList;
    }

    // 一括確認処理
    @Transactional
    public void bulkConfirm(SelectForm selectForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < selectForm.getSelectTarget().size(); i++) {
            idList.add(selectForm.getSelectTarget().get(i));
        }
        // 複数件排他ロック
        List<WeeklyReports> weeklyReportList = weeklyReportsMapper.forLockByIdList(idList);

        for (int i = 0; i < weeklyReportList.size(); i++) {
            // 更新者はセッションの社員番号
            weeklyReportList.get(i).setUpdatedBy(loginUser.getLoginUser().getUserNumber());
        }

        // 一括確認
        weeklyReportsMapper.bulkConfirm(weeklyReportList);
    }

    // 詳細情報取得
    public WeeklyReports findById(int weeklyReportId) {
        WeeklyReports weeklyReportInfo = weeklyReportsMapper.findById(weeklyReportId);
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
}
