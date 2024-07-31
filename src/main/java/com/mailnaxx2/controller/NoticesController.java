package com.mailnaxx2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mailnaxx2.constants.NoticeConstants;
import com.mailnaxx2.entity.Categories;
import com.mailnaxx2.entity.Notices;
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

    // 詳細画面初期表示
    @PostMapping("/notice/detail")
    public String detail(int noticeId,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        Notices noticeInfo = noticesService.findById(noticeId);

        // 既読フラグが'0'（未読）の場合、既読に更新する
        if (noticeInfo.getNoticeTarget().size() > 0 &&
           (noticeInfo.getNoticeTarget().get(0).getReadedFlg()).equals(NoticeConstants.UNREAD)) {
            // 既読フラグ更新処理
            noticesService.updateReadedFlg(noticeInfo.getNoticeTarget().get(0).getNoticeTargetId(), loginUser);
        }

        model.addAttribute("noticeInfo", noticeInfo);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "notice/detail";
    }

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

    // カテゴリー登録処理
    @PostMapping("/notice/createCategory")
    @ResponseBody
    public CategoriesForm createCategory(@ModelAttribute @Validated(All.class) CategoriesForm categoriesForm,
                                         BindingResult result,
                                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                categoriesForm.setErrorMessage(error.getDefaultMessage());
            }
            return categoriesForm;
        }

        // 登録
        categoriesService.insert(categoriesForm, loginUser);

        return categoriesForm;
    }

    // お知らせ登録処理
    @PostMapping("/notice/create")
    public String create(@ModelAttribute @Validated(All.class) NoticesForm noticesForm,
                         BindingResult result,
                         @ModelAttribute CategoriesForm categoriesForm,
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
