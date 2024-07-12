package com.mailnaxx2.controller;

import java.util.List;

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

import com.mailnaxx2.constants.NoticeConstants;
import com.mailnaxx2.entity.Categories;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.form.CategoriesForm;
import com.mailnaxx2.form.NoticesForm;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.CategoriesService;
import com.mailnaxx2.service.NoticesService;
import com.mailnaxx2.service.UsersService;
import com.mailnaxx2.validation.All;

@Controller
public class NoticesController {

    @Autowired
    NoticesService noticesService;

    @Autowired
    UsersService usersService;

    @Autowired
    CategoriesService categoriesService;

    // 登録画面初期表示
    @GetMapping("/admin/notice/create")
    public String create(@ModelAttribute NoticesForm noticesForm,
                         @ModelAttribute CategoriesForm categoriesForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {

        // ラジオボタン
        model.addAttribute("radioDisplayRange", NoticeConstants.RADIO);

        // 社員名プルダウン
        List<Users> userList = usersService.findAll();
        model.addAttribute("userList", userList);

        // カテゴリープルダウン
        List<Categories> categoryList = categoriesService.findAll();
        Categories category = new Categories();
        category.setCategoryId(0);
        category.setCategoryName("");
        category.setColor("");
        categoryList.add(0, category);
        model.addAttribute("categoryList", categoryList);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "notice/create";
    }

    // 登録処理
    @Transactional
    @PostMapping("/notice/create")
    public String create(@ModelAttribute @Validated(All.class) NoticesForm noticesForm,
                         @ModelAttribute CategoriesForm categoriesForm,
                         BindingResult result,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(noticesForm, categoriesForm, model, loginUser);
        }

        // 登録
        noticesService.insert(noticesForm, loginUser);

        // 一覧が必要かもしれない
        return "redirect:/admin/menu";
    }
}
