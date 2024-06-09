package com.mailnaxx2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.values.RoleClass;

@Controller
public class MenuController {

    @GetMapping("/admin/menu")
    public String index(Model model, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 総務以外は遷移できないようにする
        if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            model.addAttribute("loginUserInfo", loginUser.getLoginUser());
            return "menu/menu";
        } else {
            return "redirect:/top";
        }
    }
}
