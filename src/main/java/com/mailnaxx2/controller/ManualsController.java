package com.mailnaxx2.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.RestTemplate;

import com.mailnaxx2.constants.CommonConstants;
import com.mailnaxx2.form.ManualsForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.jackson.Manuals;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.UsersService;
import com.mailnaxx2.validation.All;
import com.mailnaxx2.validation.GroupOrder;
import com.mailnaxx2.values.RoleClass;

import jakarta.servlet.http.HttpSession;

@Controller
public class ManualsController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpSession session;

    @Autowired
    UsersService usersService;

    // マニュアル一覧
    List<Manuals> manualList;

     // マニュアル詳細
    Manuals manualInfo;

    // マニュアルAPI URL
    private static final String API_URL = "http://localhost:8081/manual-api";

    // マニュアルAPI URL（パラメータあり）
    private static final String API_URL_PARAM = "http://localhost:8081/manual-api/{id}";

    // 一覧画面初期表示
    @GetMapping("/manual/list")
    public String index(Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // マニュアル一覧を取得
        ResponseEntity<Manuals[]> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, Manuals[].class);
        manualList = Arrays.asList(response.getBody());
        System.out.println(manualList.toString());
        session.setAttribute("session_manualList", manualList);
        model.addAttribute("manualList", manualList);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/list";
    }

    // 物理削除処理
    @SuppressWarnings("unchecked")
    @Transactional
    @RequestMapping("/manual/delete")
    public String delete(@ModelAttribute SelectForm selectForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力チェック
        if (selectForm.getSelectManualId() == null) {
            // エラーメッセージを表示
            model.addAttribute("message", "対象を選択してください。");
            return index(model, loginUser);
        }

        // セッションから一覧情報を取得
        manualList = (List<Manuals>) session.getAttribute("session_manualList");

        // 権限チェック（自分か総務のみ）
        if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            // 選択したマニュアルIDを基に社員IDを取得し、判定する
            manualList.stream()
            .filter(m -> selectForm.getSelectManualId().contains(m.getManualId()))
            .collect(Collectors.toMap(
                m -> m.getManualId(),
                m -> m.getUserId()
            ))
            .forEach((manualId, userId) -> {
                if (userId == loginUser.getLoginUser().getUserId()) {
                    // 削除
                    restTemplate.delete(API_URL_PARAM, manualId);
                }
            });
            return "redirect:/manual/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("message", "権限がありません。");
            return index(model, loginUser);
        }
    }

    // 詳細画面初期表示
    @PostMapping("/manual/detail")
    public String detail(int manualId,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        ResponseEntity<Manuals> response = restTemplate.exchange(API_URL_PARAM, HttpMethod.GET, null, Manuals.class, manualId);
        manualInfo = response.getBody();
        String userName = usersService.findNameById(manualInfo.getUserId());
        model.addAttribute("manualInfo", manualInfo);
        model.addAttribute("userName", userName);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/detail";
    }

    // 登録画面初期表示
    @GetMapping("/manual/create")
    public String create(@ModelAttribute ManualsForm manualsForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        model.addAttribute("manualId", 0);
        model.addAttribute("userId", loginUser.getLoginUser().getUserId());
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/create";
    }

    // 登録処理
    @Transactional
    @PostMapping("/manual/create")
    public String create(@ModelAttribute @Validated(All.class) ManualsForm manualsForm,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(manualsForm, model, loginUser);
        }

        // リクエストヘッダーを作成
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 入力値を設定
        Manuals manual = setEntity(manualsForm);
        // レコード登録者
        manual.setCreatedBy(manualsForm.getUserNumber());
        HttpEntity<Manuals> entity = new HttpEntity<>(manual, headers);

        // 登録
        ResponseEntity<Manuals> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, Manuals.class);

        return "redirect:/manual/list";
    }

    // 編集画面初期表示
    @PostMapping("/manual/edit")
    public String edit(int manualId,
                        @ModelAttribute ManualsForm manualsForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        ResponseEntity<Manuals> response = restTemplate.exchange(API_URL_PARAM, HttpMethod.GET, null, Manuals.class, manualId);
        manualInfo = response.getBody();
        // 入力フォームに設定
        setInputForm(manualInfo, manualsForm);

        model.addAttribute("userId", manualsForm.getUserId());
        model.addAttribute("manualId", manualId);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/create";
    }

    // 更新処理
    @Transactional
    @PostMapping("/manual/update")
    public String update(int manualId,
                        @ModelAttribute @Validated(GroupOrder.class) ManualsForm manualsForm,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return edit(manualId, manualsForm, model, loginUser);
        }

        // 入力値を設定
        Manuals manual = setEntity(manualsForm);
        // マニュアルID
        manual.setManualId(manualId);
        // レコード更新者
        manual.setUpdatedBy(manualsForm.getUserNumber());
        // 更新
        restTemplate.put(API_URL_PARAM, manual, manualId);

        return "redirect:/manual/list";
    }

    // 入力値をエンティティに設定
    private Manuals setEntity(ManualsForm manualForm) {
        Manuals manual = new Manuals();
        // 社員ID
        manual.setUserId(manualForm.getUserId());
        // 表示順
        manual.setDisplayOrder(manualForm.getDisplayOrder());
        // タイトル
        manual.setTitle(manualForm.getTitle());
        // 掲載開始日
        if (manualForm.getStartYear() != "" &&
            manualForm.getStartMonth() != "" &&
            manualForm.getStartDay() != "") {
            String startYear = "%2s".formatted(manualForm.getStartYear()).replace(" ", "0");
            String startMonth = "%2s".formatted(manualForm.getStartMonth()).replace(" ", "0");
            String startDay = "%2s".formatted(manualForm.getStartDay()).replace(" ", "0");
            LocalDate startDate = LocalDate.parse(startYear + startMonth + startDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
            manual.setStartDate(startDate);
        } else {
            manual.setStartDate(LocalDate.now());
        }
        // 掲載終了日
        if (manualForm.getEndYear() != "" &&
            manualForm.getEndMonth() != "" &&
            manualForm.getEndDay() != "") {
            String endYear = "%2s".formatted(manualForm.getEndYear()).replace(" ", "0");
            String endMonth = "%2s".formatted(manualForm.getEndMonth()).replace(" ", "0");
            String endDay = "%2s".formatted(manualForm.getEndDay()).replace(" ", "0");
            LocalDate endDate = LocalDate.parse(endYear + endMonth + endDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
            manual.setEndDate(endDate);
        }
        // 内容
        manual.setContent(manualForm.getContent());
        // リンク
        manual.setLink(manualForm.getLink());

        return manual;
    }

    // 入力フォームに設定
    private void setInputForm(Manuals manualInfo, ManualsForm manualsForm) {
        manualsForm.setUserId(manualInfo.getUserId());
        manualsForm.setDisplayOrder(manualInfo.getDisplayOrder());
        manualsForm.setTitle(manualInfo.getTitle());
        String[] startDate = manualInfo.getStartDate().toString().split(CommonConstants.HALF_HYPHEN);
        manualsForm.setStartYear(startDate[0]);
        manualsForm.setStartMonth(startDate[1].replaceFirst("^0+", ""));
        manualsForm.setStartDay(startDate[2].replaceFirst("^0+", ""));
        if (manualInfo.getEndDate() != null) {
            String[] endDate = manualInfo.getEndDate().toString().split(CommonConstants.HALF_HYPHEN);
            manualsForm.setEndYear(endDate[0]);
            manualsForm.setEndMonth(endDate[1].replaceFirst("^0+", ""));
            manualsForm.setEndDay(endDate[2].replaceFirst("^0+", ""));
        }
        manualsForm.setContent(manualInfo.getContent());
        manualsForm.setLink(manualInfo.getLink());
    }
}
