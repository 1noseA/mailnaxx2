package com.mailnaxx2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Documents document = new Documents();
                //setEntity(new Users(), usersForm);

        // 作成者はセッションの社員番号
        //user.setCreatedBy(loginUser.getLoginUser().getUserNumber());

        // 登録
        documentsMapper.insert(document);
    }

    // 論理削除

    // ダウンロード

    // 一覧取得
}
