package com.mailnaxx2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mailnaxx2.form.NoticesForm;
import com.mailnaxx2.security.LoginUserDetails;

@Controller
public class NoticesController {

    // 登録画面初期表示
    @GetMapping("/admin/notice/create")
    public String create(@ModelAttribute NoticesForm noticesForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "notice/create";
    }
}
