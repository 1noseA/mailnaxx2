package com.mailnaxx2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mailnaxx2.entity.Categories;
import com.mailnaxx2.mapper.CategoriesMapper;

@Service
public class CategoriesService {

    @Autowired
    CategoriesMapper categoriesMapper;

    // 全件取得
    public List<Categories> findAll() {
        List<Categories> noticeList  = categoriesMapper.findAll();
        return noticeList;
    }
}
