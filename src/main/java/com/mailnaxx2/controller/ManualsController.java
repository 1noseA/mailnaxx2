package com.mailnaxx2.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.mailnaxx2.jackson.Manuals;
import com.mailnaxx2.security.LoginUserDetails;

@Controller
public class ManualsController {

    private final RestTemplate restTemplate;

    // RestTemplateをコンストラクタインジェクションする
    public ManualsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // マニュアルAPI 一覧取得URL
    private static final String GET_LIST_URL = "http://localhost:8081/manual-api";

    // 一覧画面初期表示
    @GetMapping("/manual/list")
    public String index(Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // マニュアル一覧を取得
        Manuals[] manualArray = restTemplate.getForObject(GET_LIST_URL, Manuals[].class);
        List<Manuals> manualList = Arrays.asList(manualArray);
        System.out.println(manualList.toString());
        model.addAttribute("manualList", manualList);

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "manual/list";
    }
}
