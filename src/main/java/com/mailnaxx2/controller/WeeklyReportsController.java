package com.mailnaxx2.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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

import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.entity.Projects;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.entity.WeeklyReports;
import com.mailnaxx2.form.SearchWeeklyReportForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.form.WeeklyReportForm;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.AffiliationsService;
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

    // 管理者
    boolean isAdmin;

    // 営業
    boolean isSales;

    // 確認済み
    boolean isConfirmed;

    // 削除権限
    boolean isDelete;

    // 週報一覧
    List<WeeklyReports> weeklyReportList;

    // 所属プルダウン
    List<Affiliations> affiliationList;

    // 担当営業プルダウン
    Set<Users> salesList;

    // 現場プルダウン
    List<Projects> projectList;

    // 報告対象週プルダウン
    Set<LocalDate> reportDateList;

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

        // 削除権限（総務・自分のみ）
        isDelete = false;
        isAdmin = false;
        if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
        	isDelete = true;
        	isAdmin = true;
        }

        // 自分の所属
        int myAffiliation = loginUser.getLoginUser().getAffiliation().getAffiliationId();

        // 週報一覧を取得
    	if (isSales || isAdmin) {
    		// 営業・総務の場合、週報を全件取得
    		weeklyReportList = weeklyReportsService.findAll();
    	} else {
    		// 営業以外は自分の所属をデフォルト表示
    		searchWeeklyReportForm.setAffiliationId(myAffiliation);
    		if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.LEADER.getCode())) {
        		// 所属長の場合、所属メンバーの週報を取得
        		weeklyReportList = weeklyReportsService.findMyAffiliation(myAffiliation);
        	} else {
        		// その他の場合、自分の週報を取得
        		weeklyReportList = weeklyReportsService.findMine(loginUser.getLoginUser().getUserId());
        		searchWeeklyReportForm.setUserName(loginUser.getLoginUser().getUserName());
        		isDelete = true;
        	}
    	}
        model.addAttribute("weeklyReportList", weeklyReportList);
        model.addAttribute("isDelete", isDelete);

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
    	// 週報一覧を取得
        weeklyReportList = weeklyReportsService.findBySearchForm(searchWeeklyReportForm);
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

        // 権限（営業のみ）
        isSales = (boolean) session.getAttribute("session_isSales");
        model.addAttribute("isSales", isSales);
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
        if (selectForm.getSelectWeeklyReportId() == null) {
            // エラーメッセージを表示
            model.addAttribute("message", "対象を選択してください。");
            return index(searchWeeklyReportForm, model, loginUser);
        }

        // 権限チェック
        if (loginUser.getLoginUser().getSalesFlg().equals("1")) {
            weeklyReportsService.bulkConfirm(selectForm, loginUser);
            return "redirect:/weekly-report/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("message", "権限がありません。");
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
        Map<String, String> radioThree = makeRadioThree();
    	for (Map.Entry<String, String> radio : radioThree.entrySet()) {
    		// 進捗状況
        	if ((radio.getKey()).equals(weeklyReportInfo.getProgress())) {
        		weeklyReportInfo.setProgress(radio.getValue());
        	}
        	// 体調
        	if ((radio.getKey()).equals(weeklyReportInfo.getCondition())) {
        		weeklyReportInfo.setCondition(radio.getValue());
        	}
        	// 人間関係
        	if ((radio.getKey()).equals(weeklyReportInfo.getRelationship())) {
        		weeklyReportInfo.setRelationship(radio.getValue());
        	}
        }
    	model.addAttribute("weeklyReportInfo", weeklyReportInfo);

        // 確認権限
    	isSales = (boolean) session.getAttribute("session_isSales");
    	if (isSales) {
    		if (weeklyReportInfo.getConfirmedFlg().equals("0")) {
    			isConfirmed = false; // 未確認
    		} else {
    			isConfirmed = true;  // 確認済み
    		}
    	}
    	model.addAttribute("isSales", isSales);
        model.addAttribute("isConfirmed", isConfirmed);

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
            model.addAttribute("message", "権限がありません。");
        }
        return detail(weeklyReportId, model, loginUser);
    }

    // 作成画面初期表示
    @SuppressWarnings("unchecked")
	@GetMapping("/weekly-report/create")
    public String create(@ModelAttribute WeeklyReportForm weeklyReportForm,
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

        // ラジオボタン作成
        Map<String, String> radioThree = makeRadioThree();
        model.addAttribute("radioProgress", radioThree);
        model.addAttribute("radioCondition", radioThree);
        model.addAttribute("radioRelationship", radioThree);

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

        // 現場社員プルダウン
        List<Users> userList = usersService.findAll();
        model.addAttribute("userList", userList);

        model.addAttribute("weeklyReportId", 0);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "weekly-report/create";
    }

    // 一時保存処理
    @PostMapping("/weekly-report/save")
    public String save(@ModelAttribute @Validated(GroupOrder.class) WeeklyReportForm weeklyReportForm,
    					BindingResult result,
    					Model model,
    					@AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(weeklyReportForm, model, loginUser);
        }

        // 登録
        weeklyReportsService.insert(weeklyReportForm, loginUser);

        return "redirect:/weekly-report/list";
    }

    // 編集画面初期表示
    @SuppressWarnings("unchecked")
	@PostMapping("/weekly-report/edit")
    public String edit(int weeklyReportId,
    				@ModelAttribute WeeklyReportForm weeklyReportForm,
    				Model model,
    				@AuthenticationPrincipal LoginUserDetails loginUser) {
    	// 詳細情報を取得
    	weeklyReportInfo = weeklyReportsService.findById(weeklyReportId);
    	// 入力フォームに設定
    	setInputForm(weeklyReportInfo, weeklyReportForm);

        // 担当営業プルダウン
    	salesList = (Set<Users>) session.getAttribute("session_salesList");
        model.addAttribute("salesList", salesList);

        // 現場プルダウン
        projectList = (List<Projects>) session.getAttribute("session_projectList");
        model.addAttribute("projectList", projectList);

        // ラジオボタン
        Map<String, String> radioThree = new LinkedHashMap<>();
        radioThree.put("1", "良い");
        radioThree.put("2", "やや良い");
        radioThree.put("3", "普通");
        radioThree.put("4", "やや悪い");
        radioThree.put("5", "悪い");
        model.addAttribute("radioProgress", radioThree);
        model.addAttribute("radioCondition", radioThree);
        model.addAttribute("radioRelationship", radioThree);

        // 現場社員プルダウン
        List<Users> userList = usersService.findAll();
        model.addAttribute("userList", userList);

        model.addAttribute("weeklyReportId", weeklyReportId);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "weekly-report/create";
    }

    // 更新処理
    @Transactional
    @PostMapping("/weekly-report/update")
    public String update(int weeklyReportId,
    					@ModelAttribute @Validated(GroupOrder.class) WeeklyReportForm weeklyReportForm,
    					BindingResult result,
    					Model model,
    					@AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
        	return edit(weeklyReportId, weeklyReportForm, model, loginUser);
        }

        WeeklyReports weeklyReport = new WeeklyReports();
        weeklyReport.setWeeklyReportId(weeklyReportId);

        // 更新
        weeklyReportsService.update(weeklyReport, weeklyReportForm, loginUser);

        return detail(weeklyReportId, model, loginUser);
    }

    // 提出処理（メール送信）
    // 一括物理削除処理
    @RequestMapping("/weekly-report/bulkDelete")
    public String bulkDelete(@ModelAttribute SelectForm selectForm,
    					SearchWeeklyReportForm searchWeeklyReportForm,
    					Model model,
    					@AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力チェック
        if (selectForm.getSelectWeeklyReportId() == null) {
            // エラーメッセージを表示
            model.addAttribute("message", "対象を選択してください。");
            return index(searchWeeklyReportForm, model, loginUser);
        }

        // 権限チェック（自分か総務のみ）
        if (loginUser.getLoginUser().getUserId() == selectForm.getSelectUserId().get(0) ||
        	loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
        	weeklyReportsService.bulkDelete(selectForm);
            return "redirect:/weekly-report/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("message", "権限がありません。");
            return index(searchWeeklyReportForm, model, loginUser);
        }
    }

    // 物理削除処理
    @RequestMapping("/weekly-report/delete")
    public String delete(@ModelAttribute SelectForm selectForm,
    					Model model,
    					@AuthenticationPrincipal LoginUserDetails loginUser) {
        // 権限チェック（自分か総務のみ）
        if (loginUser.getLoginUser().getUserId() == selectForm.getSelectUserId().get(0) ||
        	loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
        	weeklyReportsService.delete(selectForm.getSelectWeeklyReportId().get(0));
            return "redirect:/weekly-report/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("message", "権限がありません。");
            return detail(selectForm.getSelectWeeklyReportId().get(0), model, loginUser);
        }
    }

    // ラジオボタン作成
    private Map<String, String> makeRadioThree() {
    	Map<String, String> radioThree = new LinkedHashMap<>();
        radioThree.put("1", "良い");
        radioThree.put("2", "やや良い");
        radioThree.put("3", "普通");
        radioThree.put("4", "やや悪い");
        radioThree.put("5", "悪い");
        return radioThree;
    }

    // 入力フォームに設定
    private void setInputForm(WeeklyReports weeklyReportInfo, WeeklyReportForm weeklyReportForm) {
        weeklyReportForm.setSalesUserId(weeklyReportInfo.getProject().getSalesUser().getUserId());
        weeklyReportForm.setProjectId(weeklyReportInfo.getProject().getProjectId());
        weeklyReportForm.setReportDate(weeklyReportInfo.getReportDate());
        weeklyReportForm.setAveOvertimeHours(String.valueOf(weeklyReportInfo.getAveOvertimeHours()));
        weeklyReportForm.setProgress(weeklyReportInfo.getProgress());
        weeklyReportForm.setCondition(weeklyReportInfo.getCondition());
        weeklyReportForm.setRelationship(weeklyReportInfo.getRelationship());
        weeklyReportForm.setPlan(weeklyReportInfo.getPlan());
        weeklyReportForm.setWorkContent(weeklyReportInfo.getWorkContent());
        weeklyReportForm.setDifficulty(String.valueOf(weeklyReportInfo.getDifficulty()));
        weeklyReportForm.setSchedule(String.valueOf(weeklyReportInfo.getSchedule()));
        weeklyReportForm.setResult(weeklyReportInfo.getResult());
        weeklyReportForm.setImpression(weeklyReportInfo.getImpression());
        weeklyReportForm.setImprovements(weeklyReportInfo.getImprovements());
        weeklyReportForm.setNextPlan(weeklyReportInfo.getNextPlan());
        weeklyReportForm.setRemarks(weeklyReportInfo.getRemarks());
    }
}
