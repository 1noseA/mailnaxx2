package com.mailnaxx2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mailnaxx2.entity.Notices;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.NoticesService;

@Controller
public class TopController {

    @Autowired
    NoticesService noticesService;

    @RequestMapping("/top")
    public String index(Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Notices> noticeList = noticesService.findAll();
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "top/top";
    }
}
