package com.mailnaxx2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/auto-complete")
    public String getAutoComplete(){
        try {
            List<String> nameList = usersService.findAllName();
            // ListをJSON文字列に変換して直接返す
            return objectMapper.writeValueAsString(nameList);
        } catch (Exception e) {
            // 例外が発生した場合は、空のJSONオブジェクトを返す
            return "{}";
        }
    }
}
