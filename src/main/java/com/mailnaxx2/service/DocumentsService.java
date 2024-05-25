package com.mailnaxx2.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mailnaxx2.entity.Documents;
import com.mailnaxx2.form.DocumentsForm;
import com.mailnaxx2.mapper.DocumentsMapper;
import com.mailnaxx2.security.LoginUserDetails;

@Service
public class DocumentsService {

    @Autowired
    DocumentsMapper documentsMapper;

    // 登録
    @Transactional
    public void insert(DocumentsForm documentsForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        // エンティティにセットする
        Documents document = setEntity(new Documents(), documentsForm);

        // 作成者はセッションの社員番号
        document.setCreatedBy(loginUser.getLoginUser().getUserNumber());

        // 登録
        documentsMapper.insert(document);
    }

    // エンティティにセットする
    private Documents setEntity(Documents document, DocumentsForm documentsForm) {
        // ファイル名
        document.setFileName((documentsForm.getFileData()).getOriginalFilename());

        // 表示名
        document.setDisplayName(documentsForm.getDisplayName());

        // ファイルデータ
        document.setFileData(getByteFile(documentsForm.getFileData()));

        return document;
    }

    // アップロードファイルをバイトに変換
    private byte[] getByteFile(MultipartFile multipartFile) {
        try {
            // アップロードファイルをバイト値に変換
            byte[] bytes = multipartFile.getBytes();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 論理削除

    // ダウンロード

    // 一覧取得
}
