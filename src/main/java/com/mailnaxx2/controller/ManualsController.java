package com.mailnaxx2.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private final RestTemplate restTemplate;

    // RestTemplateをコンストラクタインジェクションする
    public ManualsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    // 一覧画面初期表示
    @GetMapping("/manual/list")
    public String index(Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // マニュアル一覧を取得
        Manuals[] manualArray = restTemplate.getForObject(API_URL, Manuals[].class);
        manualList = Arrays.asList(manualArray);
        System.out.println(manualList.toString());
        session.setAttribute("session_manualList", manualList);
        model.addAttribute("manualList", manualList);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/list";
    }

    // 物理削除処理
    @SuppressWarnings("unchecked")
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
                    restTemplate.delete(API_URL + "/" + manualId, manualId);
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
        manualInfo = restTemplate.getForObject(API_URL + "/" + manualId, Manuals.class);
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
    @PostMapping("/manual/create")
    public String create(@ModelAttribute @Validated(All.class) ManualsForm manualsForm,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(manualsForm, model, loginUser);
        }

        // 登録
        manualInfo = restTemplate.postForObject(API_URL, manualsForm, Manuals.class);

        return "redirect:/manual/list";
    }

    // 編集画面初期表示
    @PostMapping("/manual/edit")
    public String edit(int manualId,
                        @ModelAttribute ManualsForm manualsForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        manualInfo = restTemplate.getForObject(API_URL + "/" + manualId, Manuals.class);
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

        // 更新
        manualInfo = restTemplate.patchForObject(API_URL + "/" + manualId, manualsForm, Manuals.class);

        return "redirect:/manual/list";
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
