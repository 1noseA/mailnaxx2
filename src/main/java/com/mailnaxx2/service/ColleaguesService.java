package com.mailnaxx2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mailnaxx2.entity.Colleagues;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.ColleagueForm;
import com.mailnaxx2.mapper.ColleaguesMapper;

@Service
public class ColleaguesService {

    @Autowired
    ColleaguesMapper colleaguesMapper;

    // 週報一覧
    List<WeeklyReports> weeklyReportList;

    // 週報詳細
    WeeklyReports weeklyReportInfo;

    // 登録処理
    @Transactional
    public int insert(ColleagueForm colleagueForm) {
        // 入力値をセットする
        Colleagues colleague = setColleagueForm(new Colleagues(), colleagueForm);

        // 現場社員登録
        colleaguesMapper.insert(colleague);

        // 登録した現場社員IDを取得
        int colleagueId = colleague.getColleagueId();
        return colleagueId;
    }

    // 入力値をセットする
    private Colleagues setColleagueForm(Colleagues colleague, ColleagueForm colleagueForm) {
        // 現場社員ID
        Users user = new Users();
        user.setUserId(colleagueForm.getColleagueUserId());
        colleague.setUser(user);

        // 難易度
        colleague.setDifficulty(colleagueForm.getColleagueDifficulty());

        // スケジュール感
        colleague.setSchedule(colleagueForm.getColleagueSchedule());

        // 所感
        colleague.setImpression(colleagueForm.getColleagueImpression());

        return colleague;
    }
}
