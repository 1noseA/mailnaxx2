package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Notices;

@Mapper
public interface NoticesMapper {

    // 全件取得
    public List<Notices> findAll();

    // ログインユーザのお知らせ取得
    public List<Notices> findByLoginUser(int userId);

    // 1件取得
    public Notices findById(int noticeId);

    // 登録
    public int insert(Notices notice);
}
