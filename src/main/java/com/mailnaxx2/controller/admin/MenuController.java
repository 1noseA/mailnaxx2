package com.mailnaxx2.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mailnaxx2.security.LoginUserDetails;

@Controller
public class MenuController {

    @GetMapping("/admin/menu")
    public String index(Model model, @AuthenticationPrincipal LoginUserDetails loginUser) {
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "admin/menu/menu";
    }
}
