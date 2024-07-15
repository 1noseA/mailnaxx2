package com.mailnaxx2.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mailnaxx2.entity.Categories;
import com.mailnaxx2.form.CategoriesForm;
import com.mailnaxx2.mapper.CategoriesMapper;
import com.mailnaxx2.security.LoginUserDetails;

@Service
public class CategoriesService {

    @Autowired
    CategoriesMapper categoriesMapper;

    // 全件取得
    public List<Categories> findAll() {
        List<Categories> noticeList  = categoriesMapper.findAll();
        return noticeList;
    }

    // 登録処理
    @Transactional
    public void insert(CategoriesForm categoriesForm,
                       @AuthenticationPrincipal LoginUserDetails loginUser) {
        // エンティティにセットする
        Categories category = setEntity(new Categories(), categoriesForm);

        // 作成者はセッションの社員番号
        category.setCreatedBy(loginUser.getLoginUser().getUserNumber());

        // 登録
        categoriesMapper.insert(category);

        categoriesForm.setCategoryId(category.getCategoryId());
    }

    // エンティティにセットする
    private Categories setEntity(Categories category, CategoriesForm categoriesForm) {
        // カテゴリー名
        category.setCategoryName(categoriesForm.getCategoryName());

        // 色
        if (!StringUtils.isEmpty(categoriesForm.getColor())) {
            category.setColor(categoriesForm.getColor());
        }

        return category;
    }
}
