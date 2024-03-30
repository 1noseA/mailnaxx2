package com.mailnaxx2.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.mailnaxx2.form.ManualsForm;
import com.mailnaxx2.jackson.Manuals;
import com.mailnaxx2.security.LoginUserDetails;

@Controller
public class ManualsController {

    private final RestTemplate restTemplate;

    // RestTemplateをコンストラクタインジェクションする
    public ManualsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // マニュアル一覧
    List<Manuals> manualList;

     // マニュアル詳細
    Manuals manualInfo;

    // マニュアルAPI 情報取得URL
    private static final String GET_URL = "http://localhost:8081/manual-api";

    // 一覧画面初期表示
    @GetMapping("/manual/list")
    public String index(Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // マニュアル一覧を取得
        Manuals[] manualArray = restTemplate.getForObject(GET_URL, Manuals[].class);
        manualList = Arrays.asList(manualArray);
        System.out.println(manualList.toString());
        model.addAttribute("manualList", manualList);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/list";
    }

    // 詳細画面初期表示
    @PostMapping("/manual/detail")
    public String detail(int manualId,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        manualInfo = restTemplate.getForObject(GET_URL + "/" + manualId, Manuals.class);
        model.addAttribute("manualInfo", manualInfo);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/detail";
    }

    // 登録画面初期表示
    @GetMapping("/manual/create")
    public String create(@ModelAttribute ManualsForm manualsForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        model.addAttribute("manualId", 0);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/create";
    }
}
