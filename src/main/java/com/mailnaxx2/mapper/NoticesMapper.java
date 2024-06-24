package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Notices;

@Mapper
public interface NoticesMapper {

    // 全件取得
    public List<Notices> findAll();

    // 登録
    public int insert(Notices notice);
}
