package com.mailnaxx2.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Documents;

@Mapper
public interface DocumentsMapper {

    // 登録
    public int insert(Documents document);

    // 論理削除
    public void delete(List<Documents> documentList);

    // 複数件排他ロック
    public List<Documents> forLockByIdList(List<Integer> idList);

    // 一覧取得
    public List<Documents> findAll();

    // 1件取得
    public Documents findById(int documentId);

    // ダウンロード
}
