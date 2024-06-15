package com.mailnaxx2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailnaxx2.service.UsersService;

@RestController
public class AutoCompleteController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UsersService usersService;

    /** 全商品名をオートコンプリートに渡す. */
    @GetMapping("/auto-complete")
    public ResponseEntity<String> getAutoComplete(){
        try {
            List<String> nameList = usersService.findAllName();
            // ListをJSON文字列に変換
            String json = objectMapper.writeValueAsString(nameList);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            // 例外処理
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
