package com.mailnaxx2.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mailnaxx2.constants.WeeklyReportConstants;
import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.entity.Colleagues;
import com.mailnaxx2.entity.Projects;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.ColleagueForm;
import com.mailnaxx2.form.DetailForm;
import com.mailnaxx2.form.SearchWeeklyReportForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.form.WeeklyReportForm;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.AffiliationsService;
import com.mailnaxx2.service.ColleaguesService;
import com.mailnaxx2.service.ProjectsService;
import com.mailnaxx2.service.UsersService;
import com.mailnaxx2.service.WeeklyReportsService;
import com.mailnaxx2.validation.GroupOrder;
import com.mailnaxx2.values.RoleClass;

@Controller
public class WeeklyReportsController {

    @Autowired
    HttpSession session;

    @Autowired
    UsersService usersService;

    @Autowired
    WeeklyReportsService weeklyReportsService;

    @Autowired
    AffiliationsService affiliationsService;

    @Autowired
    ProjectsService projectsService;

    @Autowired
    ColleaguesService colleaguesService;

    // 営業
    boolean isSales;

    // 上長（マネジャー・リーダー・チーフ）
    boolean isBoss;

    // 一般
    boolean isMember;

    // 確認済み
    boolean isConfirmed;

    // 共有済み
    boolean isShared;

    // 削除権限
    boolean isDelete;

    // 週報一覧
    List<WeeklyReports> weeklyReportList;

    // 現場社員情報
    Colleagues colleagueInfo;

    // 所属プルダウン
    List<Affiliations> affiliationList;

    // 担当営業プルダウン
    Set<Users> salesList;

    // 現場プルダウン
    List<Projects> projectList;

    // 報告対象週プルダウン
    Set<LocalDate> reportDateList;

    // 現場社員プルダウン
    List<Users> userList;

    // 週報詳細
    WeeklyReports weeklyReportInfo;

    // 一覧画面初期表示
    @RequestMapping("/weekly-report/list")
    public String index(SearchWeeklyReportForm searchWeeklyReportForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 確認権限（営業のみ）
        isSales = false;
        if (loginUser.getLoginUser().getSalesFlg().equals("1")) {
            isSales = true;
        }
        session.setAttribute("session_isSales", isSales);
        model.addAttribute("isSales", isSales);

        // 自分の所属
        int myAffiliation = loginUser.getLoginUser().getAffiliation().getAffiliationId();

        // 週報一覧を取得
        isBoss = false;
        isMember = false;
        if (isSales || loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            // 営業・総務の場合、週報を全件取得
            weeklyReportList = weeklyReportsService.findAll();
        } else {
            // 営業・総務以外は自分の所属をデフォルト表示
            searchWeeklyReportForm.setAffiliationId(myAffiliation);
            if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.MANAGER.getCode()) ||
                loginUser.getLoginUser().getRoleClass().equals(RoleClass.LEADER.getCode())) {
                // 所属長の場合、所属メンバーの週報を取得
                weeklyReportList = weeklyReportsService.findMyAffiliation(myAffiliation);
                isBoss = true;
            } else {
                // その他の場合、自分の週報を取得
                weeklyReportList = weeklyReportsService.findMine(loginUser.getLoginUser().getUserId());
                searchWeeklyReportForm.setUserName(loginUser.getLoginUser().getUserName());
                isMember = true;
            }
        }
        session.setAttribute("session_isBoss", isBoss);
        session.setAttribute("session_isMember", isMember);
        model.addAttribute("isBoss", isBoss);
        model.addAttribute("isMember", isMember);

        // 自分の一時保存データがある場合、メッセージを表示
        if (weeklyReportList.stream()
                .filter(w -> w.getUser().getUserId() == (loginUser.getLoginUser().getUserId()))
                .anyMatch(w -> w.getStatus().contains("1"))) {
            model.addAttribute("infoMessage", "一時保存中の週次報告書があります。");
        }

        // ステータスを表示名に変換
        weeklyReportList.forEach(w -> w.setStatus(convertDisplayStatus(w.getStatus())));
        model.addAttribute("weeklyReportList", weeklyReportList);

        // 所属プルダウン
        affiliationList = affiliationsService.findAll();
        session.setAttribute("session_affiliationList", affiliationList);
        model.addAttribute("affiliationList", affiliationList);

        // 担当営業プルダウン
        projectList = projectsService.findAll();
        session.setAttribute("session_projectList", projectList);
        salesList = new LinkedHashSet<>();
        for (Projects p : projectList) {
            salesList.add(p.getSalesUser());
        }
        session.setAttribute("session_salesList", salesList);
        model.addAttribute("salesList", salesList);

        // 報告対象週プルダウン
        reportDateList = new LinkedHashSet<>();
        for (WeeklyReports w : weeklyReportList) {
            reportDateList.add(w.getReportDate());
        }
        session.setAttribute("session_reportDateList", reportDateList);
        model.addAttribute("reportDateList", reportDateList);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "weekly-report/list";
    }

    // 検索処理
    @SuppressWarnings("unchecked")
    @PostMapping("/weekly-report/search")
    public String search(SearchWeeklyReportForm searchWeeklyReportForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 権限（営業のみ）
        isSales = (boolean) session.getAttribute("session_isSales");
        model.addAttribute("isSales", isSales);

        // 上長
        isBoss  = (boolean) session.getAttribute("session_isBoss");
        model.addAttribute("isBoss", isBoss);

        // 一般
        isMember  = (boolean) session.getAttribute("session_isMember");
        model.addAttribute("isMember", isMember);

        // 週報一覧を取得
        if (isMember) {
            weeklyReportList = weeklyReportsService.findByMemberSearchForm(searchWeeklyReportForm);
        } else {
            weeklyReportList = weeklyReportsService.findBySearchForm(searchWeeklyReportForm);
        }

        // 自分の一時保存データがある場合、メッセージを表示
         if (weeklyReportList.stream()
                 .filter(w -> w.getUser().getUserId() == (loginUser.getLoginUser().getUserId()))
                 .anyMatch(w -> w.getStatus().contains("1"))) {
             model.addAttribute("infoMessage", "一時保存中の週次報告書があります。");
         }

         // ステータスを表示名に変換
         weeklyReportList.forEach(w -> w.setStatus(convertDisplayStatus(w.getStatus())));
        model.addAttribute("weeklyReportList", weeklyReportList);

        // 所属プルダウン
        affiliationList = (List<Affiliations>) session.getAttribute("session_affiliationList");
        model.addAttribute("affiliationList", affiliationList);

        // 担当営業プルダウン
        salesList = (Set<Users>) session.getAttribute("session_salesList");
        model.addAttribute("salesList", salesList);

        // 報告対象週プルダウン
        reportDateList = (Set<LocalDate>) session.getAttribute("session_reportDateList");
        model.addAttribute("reportDateList", reportDateList);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "weekly-report/list";
    }

    // 一括確認処理（営業のみ）
    @PostMapping("/weekly-report/bulkConfirm")
    public String bulkConfirm(@ModelAttribute SelectForm selectForm,
                              SearchWeeklyReportForm searchWeeklyReportForm,
                              Model model,
                              @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力チェック
        if (selectForm.getSelectId() == null) {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "対象を選択してください。");
            return index(searchWeeklyReportForm, model, loginUser);
        }

        // 権限チェック
        if (loginUser.getLoginUser().getSalesFlg().equals("1")) {
            weeklyReportsService.bulkConfirm(selectForm, loginUser);
            return "redirect:/weekly-report/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "権限がありません。");
            return index(searchWeeklyReportForm, model, loginUser);
        }
    }

    // 詳細画面初期表示
    @PostMapping("/weekly-report/detail")
    public String detail(int weeklyReportId,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        weeklyReportInfo = weeklyReportsService.findById(weeklyReportId);
        // ラジオボタン項目
        weeklyReportInfo.setProgress(WeeklyReportConstants.RADIO.get(weeklyReportInfo.getProgress()));
        weeklyReportInfo.setCondition(WeeklyReportConstants.RADIO.get(weeklyReportInfo.getCondition()));
        weeklyReportInfo.setRelationship(WeeklyReportConstants.RADIO.get(weeklyReportInfo.getRelationship()));
        model.addAttribute("weeklyReportInfo", weeklyReportInfo);

        // 削除権限（総務・自分のみ）
        isDelete = false;
        if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            isDelete = true;
        }

        // 自分の週報の場合
        boolean isAuthenticated = false;
        if (weeklyReportInfo.getUser().getUserId() == loginUser.getLoginUser().getUserId()) {
            isAuthenticated = true;
            isDelete = true;
        }
        model.addAttribute("isDelete", isDelete);
        model.addAttribute("isAuthenticated", isAuthenticated);

        // 確認権限
        isSales = (boolean) session.getAttribute("session_isSales");
        if (isSales) {
            if (weeklyReportInfo.getStatus().equals("3")) {
                isConfirmed = true;  // 確認済み
            } else {
                isConfirmed = false; // 未確認
            }
        }
        model.addAttribute("isSales", isSales);
        model.addAttribute("isConfirmed", isConfirmed);

        // 共有ボタン表示制御
        isShared = false;
        if (weeklyReportInfo.getSharedFlg().equals("1")) {
            isShared = true;
        }
        model.addAttribute("isShared", isShared);

        // 既読処理
        isBoss  = (boolean) session.getAttribute("session_isBoss");
        if (isBoss) {
            weeklyReportsService.readed(weeklyReportId, loginUser);
        }

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "weekly-report/detail";
    }

    // 確認処理（営業のみ）
    @PostMapping("/weekly-report/confirm")
    public String confirm(int weeklyReportId,
                          Model model,
                          @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 権限チェック
        if (loginUser.getLoginUser().getSalesFlg().equals("1")) {
            weeklyReportsService.confirm(weeklyReportId, loginUser);
        } else {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "権限がありません。");
        }
        return detail(weeklyReportId, model, loginUser);
    }

    // コメント処理（営業のみ）
    @PostMapping("/weekly-report/comment")
    public String comment(@ModelAttribute DetailForm detailForm,
                          Model model,
                          @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 権限チェック
        if (loginUser.getLoginUser().getSalesFlg().equals("1")) {
            weeklyReportsService.comment(detailForm, loginUser);
        } else {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "権限がありません。");
        }
        return detail(detailForm.getWeeklyReportId(), model, loginUser);
    }

    // 共有処理（営業のみ）
    @PostMapping("/weekly-report/share")
    public String share(int weeklyReportId,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 権限チェック
        if (loginUser.getLoginUser().getSalesFlg().equals("1")) {
            weeklyReportsService.share(weeklyReportId, loginUser);
        } else {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "権限がありません。");
        }
        return detail(weeklyReportId, model, loginUser);
    }

    // 作成画面初期表示
    @SuppressWarnings("unchecked")
    @GetMapping("/weekly-report/create")
    public String create(@ModelAttribute WeeklyReportForm weeklyReportForm,
                         @ModelAttribute ColleagueForm colleagueForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 担当営業プルダウン
        salesList = (Set<Users>) session.getAttribute("session_salesList");
        model.addAttribute("salesList", salesList);

        // 現場プルダウン
        projectList = (List<Projects>) session.getAttribute("session_projectList");
        model.addAttribute("projectList", projectList);

        // 報告対象週ラベル
        // 現在日付を取得
        LocalDate now = LocalDate.now();
        // 現在日の週の月曜日を取得
        LocalDate reportDate = now.with(DayOfWeek.MONDAY);
        weeklyReportForm.setReportDate(reportDate);

        // ラジオボタン
        model.addAttribute("radioProgress", WeeklyReportConstants.RADIO);
        model.addAttribute("radioCondition", WeeklyReportConstants.RADIO);
        model.addAttribute("radioRelationship", WeeklyReportConstants.RADIO);

        // 初期値
        weeklyReportForm.setDifficulty(100);
        weeklyReportForm.setSchedule(100);

        // 先週分の週報を取得
        LocalDate lastWeek = reportDate.minusDays(7);
        WeeklyReports lastWeekReportInfo = weeklyReportsService.findByLastWeek(loginUser.getLoginUser().getUserId(), lastWeek);
        // 先週分がある場合、以下の項目は自動入力
        if (lastWeekReportInfo != null) {
            // 担当営業
            weeklyReportForm.setSalesUserId(lastWeekReportInfo.getProject().getSalesUser().getUserId());
            // 現場
            weeklyReportForm.setProjectId(lastWeekReportInfo.getProject().getProjectId());
            // 今週の目標
            weeklyReportForm.setPlan(lastWeekReportInfo.getNextPlan());
        }

        model.addAttribute("weeklyReportId", 0);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "weekly-report/create";
    }

    // 現場社員取得処理(ajax)
    @PostMapping("/weekly-report/getColleague")
    @ResponseBody
    public List<Users> getColleague(String userNumber, int projectId) {
        // 現場社員プルダウン
        userList = usersService.findColleague(userNumber, projectId);
        return userList;
    }

    // 現場社員登録処理(ajax)
    @PostMapping("/weekly-report/saveColleague")
    @ResponseBody
    public ColleagueForm saveColleague(int colleagueUserId, int colleagueDifficulty, int colleagueSchedule, String colleagueImpression) {
        // TODO：Formごと送信できるようにしたい
        ColleagueForm colleagueForm = new ColleagueForm();
        colleagueForm.setColleagueUserId(colleagueUserId);
        colleagueForm.setColleagueDifficulty(colleagueDifficulty);
        colleagueForm.setColleagueSchedule(colleagueSchedule);
        colleagueForm.setColleagueImpression(colleagueImpression);

        // 登録
        int colleagueId = colleaguesService.insert(colleagueForm);
        colleagueForm.setColleagueId(colleagueId);
        return colleagueForm;
    }

    // 一時保存処理
    @PostMapping("/weekly-report/save")
    public String save(@ModelAttribute @Validated(GroupOrder.class) WeeklyReportForm weeklyReportForm,
                       BindingResult result,
                       @ModelAttribute ColleagueForm colleagueForm,
                       Model model,
                       @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(weeklyReportForm, colleagueForm, model, loginUser);
        }

        // 一時保存
        weeklyReportForm.setStatus("1");
        // 週報登録
        weeklyReportsService.insert(weeklyReportForm, loginUser);

        return "redirect:/weekly-report/list";
    }

    // 編集画面初期表示
    @SuppressWarnings("unchecked")
    @PostMapping("/weekly-report/edit")
    public String edit(int weeklyReportId,
                       @ModelAttribute WeeklyReportForm weeklyReportForm,
                       @ModelAttribute ColleagueForm colleagueForm,
                       Model model,
                       @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        weeklyReportInfo = weeklyReportsService.findById(weeklyReportId);
        // 入力フォームに設定
        setInputForm(weeklyReportInfo, weeklyReportForm);

        // 現場社員を取得
        colleagueInfo = colleaguesService.findById(weeklyReportId);
        // 入力フォームに設定
        if (colleagueInfo != null) {
            setInputForm(colleagueInfo, colleagueForm);
        }

        // 担当営業プルダウン
        salesList = (Set<Users>) session.getAttribute("session_salesList");
        model.addAttribute("salesList", salesList);

        // 現場プルダウン
        projectList = (List<Projects>) session.getAttribute("session_projectList");
        model.addAttribute("projectList", projectList);

        // ラジオボタン
        model.addAttribute("radioProgress", WeeklyReportConstants.RADIO);
        model.addAttribute("radioCondition", WeeklyReportConstants.RADIO);
        model.addAttribute("radioRelationship", WeeklyReportConstants.RADIO);

        model.addAttribute("weeklyReportId", weeklyReportId);
        model.addAttribute("colleagueInfo", colleagueInfo);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "weekly-report/create";
    }

    // 更新処理（ステータスは変えない）
    @Transactional
    @PostMapping("/weekly-report/update")
    public String update(@ModelAttribute @Validated(GroupOrder.class) WeeklyReportForm weeklyReportForm,
                         BindingResult result,
                         @ModelAttribute ColleagueForm colleagueForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        int weeklyReportId = weeklyReportForm.getWeeklyReportId();
        // 入力エラーチェック
        if (result.hasErrors()) {
            return edit(weeklyReportId, weeklyReportForm, colleagueForm, model, loginUser);
        }

        // 更新
        weeklyReportsService.update(weeklyReportForm, loginUser);

        return detail(weeklyReportId, model, loginUser);
    }

    // 提出処理（メール送信）
    @PostMapping("/weekly-report/send")
    public String send(@ModelAttribute @Validated(GroupOrder.class) WeeklyReportForm weeklyReportForm,
                       BindingResult result,
                       @ModelAttribute ColleagueForm colleagueForm,
                       Model model,
                       @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(weeklyReportForm, colleagueForm, model, loginUser);
        }

        // 提出済み
        weeklyReportForm.setStatus("2");
        if (weeklyReportForm.getWeeklyReportId() == 0) {
            // 登録
            weeklyReportsService.insert(weeklyReportForm, loginUser);
        } else {
            // 更新
            weeklyReportsService.update(weeklyReportForm, loginUser);
        }

        // メール送信処理

        return "redirect:/weekly-report/list";
    }

    // 一括物理削除処理
    @RequestMapping("/weekly-report/bulkDelete")
    public String bulkDelete(@ModelAttribute SelectForm selectForm,
                             SearchWeeklyReportForm searchWeeklyReportForm,
                             Model model,
                             @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力チェック
        if (selectForm.getSelectId() == null) {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "対象を選択してください。");
            return index(searchWeeklyReportForm, model, loginUser);
        }

        // 権限チェック（自分か総務のみ）
        if (loginUser.getLoginUser().getUserId() == selectForm.getSelectUserId().get(0) ||
            loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            weeklyReportsService.bulkDelete(selectForm);
            return "redirect:/weekly-report/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "権限がありません。");
            return index(searchWeeklyReportForm, model, loginUser);
        }
    }

    // 物理削除処理
    @RequestMapping("/weekly-report/delete")
    public String delete(@ModelAttribute DetailForm detailForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 権限チェック（自分か総務のみ）
        if (loginUser.getLoginUser().getUserId() == detailForm.getUserId() ||
            loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            weeklyReportsService.delete(detailForm.getWeeklyReportId());
            return "redirect:/weekly-report/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("errorMessage", "権限がありません。");
            return detail(detailForm.getWeeklyReportId(), model, loginUser);
        }
    }

    // ステータスを表示名に変換
    private String convertDisplayStatus(String status) {
        // キー（コード値）にマップされた値（表示名）を返却（キーがない場合は""）
        return WeeklyReportConstants.STATUS.getOrDefault(status, "");
    }

    // 入力フォームに設定（週報）
    private void setInputForm(WeeklyReports weeklyReportInfo, WeeklyReportForm weeklyReportForm) {
        weeklyReportForm.setWeeklyReportId(weeklyReportInfo.getWeeklyReportId());
        weeklyReportForm.setStatus(weeklyReportInfo.getStatus());
        weeklyReportForm.setSalesUserId(weeklyReportInfo.getProject().getSalesUser().getUserId());
        weeklyReportForm.setProjectId(weeklyReportInfo.getProject().getProjectId());
        weeklyReportForm.setReportDate(weeklyReportInfo.getReportDate());
        weeklyReportForm.setAveOvertimeHours(weeklyReportInfo.getAveOvertimeHours());
        weeklyReportForm.setProgress(weeklyReportInfo.getProgress());
        weeklyReportForm.setCondition(weeklyReportInfo.getCondition());
        weeklyReportForm.setRelationship(weeklyReportInfo.getRelationship());
        weeklyReportForm.setPlan(weeklyReportInfo.getPlan());
        weeklyReportForm.setWorkContent(weeklyReportInfo.getWorkContent());
        weeklyReportForm.setDifficulty(weeklyReportInfo.getDifficulty());
        weeklyReportForm.setSchedule(weeklyReportInfo.getSchedule());
        weeklyReportForm.setResult(weeklyReportInfo.getResult());
        weeklyReportForm.setImpression(weeklyReportInfo.getImpression());
        weeklyReportForm.setImprovements(weeklyReportInfo.getImprovements());
        weeklyReportForm.setNextPlan(weeklyReportInfo.getNextPlan());
        weeklyReportForm.setRemarks(weeklyReportInfo.getRemarks());
    }

    // 入力フォームに設定（現場社員）
    private void setInputForm(Colleagues colleagueInfo, ColleagueForm colleagueForm) {
        colleagueForm.setColleagueId(colleagueInfo.getColleagueId());
        colleagueForm.setWeeklyReportId(colleagueInfo.getWeeklyReport().getWeeklyReportId());
        colleagueForm.setColleagueUserId(colleagueInfo.getUser().getUserId());
        colleagueForm.setColleagueDifficulty(colleagueInfo.getDifficulty());
        colleagueForm.setColleagueSchedule(colleagueInfo.getSchedule());
        colleagueForm.setColleagueImpression(colleagueInfo.getImpression());
    }
}
