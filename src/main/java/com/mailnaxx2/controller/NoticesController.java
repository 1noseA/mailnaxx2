package com.mailnaxx2.controller;

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

import com.mailnaxx2.form.NoticesForm;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.NoticesService;
import com.mailnaxx2.validation.All;

@Controller
public class NoticesController {

    @Autowired
    NoticesService noticesService;

    // 登録画面初期表示
    @GetMapping("/admin/notice/create")
    public String create(@ModelAttribute NoticesForm noticesForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "notice/create";
    }

    // 登録処理
    @Transactional
    @PostMapping("/notice/create")
    public String create(@ModelAttribute @Validated(All.class) NoticesForm noticesForm,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(noticesForm, model, loginUser);
        }

        // 登録
        noticesService.insert(noticesForm, loginUser);

        // 一覧が必要かもしれない
        return "redirect:/admin/menu";
    }
}
