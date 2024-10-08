package com.mailnaxx2.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.mailnaxx2.entity.Users;
import com.mailnaxx2.mapper.UsersMapper;
import com.mailnaxx2.security.LoginUserDetails;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    HttpSession session;

    @Autowired
    UsersMapper usersMapper;

    @GetMapping("/")
    public String showLoginPage() {
        return "login/login";
    }

    // テストログイン
    @PostMapping("/testLogin")
    public String testLogin() {
        String userNumber = "20231001";
        // 最終ログイン日時の更新とログイン失敗回数初期化
        usersMapper.loginSuccess(userNumber);
        Optional<Users> user = usersMapper.findLoginUser(userNumber);
        UserDetails userDetails = user.map(users -> new LoginUserDetails(users))
                .orElseThrow(() -> new UsernameNotFoundException("ログインに失敗しました。"));

        // セキュリティコンテキストの内容を更新
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "forward:/top";
    }

    @GetMapping("/reset")
    public String showResetPage(Model model) {
        return "login/reset";
    }

    @GetMapping("/logout")
    public String logout() {
        session.removeAttribute("session_isAdmin");
        session.removeAttribute("session_isSales");
        session.removeAttribute("session_isBoss");
        session.removeAttribute("session_isMember");
        session.removeAttribute("session_affiliationList");
        session.removeAttribute("session_salesList");
        session.removeAttribute("session_reportDateList");
        session.removeAttribute("session_projectList");
        return "/";
    }
}