package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Categories;

@Mapper
public interface CategoriesMapper {

    // 全件取得
    public List<Categories> findAll();

    // 登録
    public int insert(Categories category);
}
