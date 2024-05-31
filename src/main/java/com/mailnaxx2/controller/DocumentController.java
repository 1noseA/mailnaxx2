package com.mailnaxx2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mailnaxx2.entity.Documents;
import com.mailnaxx2.form.DocumentsForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.DocumentsService;

@Controller
public class DocumentController {

    @Autowired
    DocumentsService documentsService;

    // アップロード画面初期表示
    @GetMapping("/admin/document/upload")
    public String upload(@ModelAttribute DocumentsForm documentsForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 資料一覧取得
        List<Documents> documentList = documentsService.findAll();
        model.addAttribute("documentList", documentList);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "document/upload";
    }

    // アップロード処理
    @PostMapping("/admin/document/upload")
    public String upload(@ModelAttribute @Validated DocumentsForm documentsForm,
                         BindingResult result,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        boolean errFlg = false;
        // ファイルの存在チェック
        if (documentsForm.getFileData().isEmpty()) {
            result.rejectValue("fileData", "NotBlank", "ファイルを選択してください");
            errFlg = true;
        }

        // 入力エラーチェック
        if (result.hasErrors()) {
            errFlg = true;
        }

        if (errFlg) {
            return upload(documentsForm, model, loginUser);
        }

        // 登録
        documentsService.insert(documentsForm, loginUser);

        // Formの初期化
        model.addAttribute("documentsForm", new DocumentsForm());
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "document/upload";
    }

    // 論理削除処理
    @PostMapping("/admin/document/delete")
    public String delete(@ModelAttribute SelectForm selectForm,
                         DocumentsForm documentsForm,
                         Model model,
                         @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力チェック
        if (selectForm.getSelectId() == null) {
            // エラーメッセージを表示
            model.addAttribute("message", "対象を選択してください。");
            return upload(documentsForm, model, loginUser);
        }
        documentsService.delete(selectForm, loginUser);
        return upload(documentsForm, model, loginUser);
    }

    // 資料一覧画面初期表示
    @GetMapping("/document/list")
    public String index(Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 資料一覧取得
        List<Documents> documentList = documentsService.findAll();
        model.addAttribute("documentList", documentList);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "document/list";
    }
}
