package com.mailnaxx2.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mailnaxx2.form.DocumentsForm;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.DocumentsService;

@Controller
public class DocumentController {

    @Autowired
    DocumentsService documentsService;

    @GetMapping("/admin/document/upload")
    public String index(@ModelAttribute DocumentsForm documentsForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "admin/document/upload";
    }

    // アップロード処理
    @PostMapping("/admin/document/upload")
    public String upload(DocumentsForm documentsForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 登録
        documentsService.insert(documentsForm, loginUser);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "admin/document/upload";
    }
}
