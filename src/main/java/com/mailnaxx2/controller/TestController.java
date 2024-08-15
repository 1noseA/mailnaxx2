package com.mailnaxx2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mailnaxx2.constants.UserConstants;
import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.form.UsersForm;
import com.mailnaxx2.service.AffiliationsService;
import com.mailnaxx2.service.UsersService;
import com.mailnaxx2.validation.All;
import com.mailnaxx2.values.RoleClass;

@Controller
public class TestController {

    @Autowired
    UsersService usersService;

    @Autowired
    AffiliationsService affiliationsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 登録画面初期表示
    @GetMapping("/test/create")
    public String create(@ModelAttribute UsersForm usersForm,
                         Model model) {

        // 所属プルダウン
        List<Affiliations> affiliationList = affiliationsService.findAll();
        model.addAttribute("affiliationList", affiliationList);
        model.addAttribute("notAffiliation", UserConstants.NOT_AFFILIATION);

        // 権限区分プルダウン
        model.addAttribute("roleClassList", RoleClass.values());
        return "test/create";
    }

    // 登録画面登録処理
    @PostMapping("/test/create")
    public String create(@ModelAttribute @Validated(All.class) UsersForm usersForm,
                         BindingResult result, Model model) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(usersForm, model);
        }

        // テスト登録サービス実行
        usersService.testInsert(usersForm);
        return "login/login";
    }
}
