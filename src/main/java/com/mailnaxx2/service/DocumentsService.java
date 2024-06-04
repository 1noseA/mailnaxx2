package com.mailnaxx2.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mailnaxx2.constants.CommonConstants;
import com.mailnaxx2.entity.Documents;
import com.mailnaxx2.form.DocumentsForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.mapper.DocumentsMapper;
import com.mailnaxx2.security.LoginUserDetails;

@Service
public class DocumentsService {

    @Autowired
    DocumentsMapper documentsMapper;

    // 登録処理
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
        String fileName = (documentsForm.getFileData()).getOriginalFilename();
        document.setFileName(fileName);

        // 表示名
        if (StringUtils.isEmpty(documentsForm.getDisplayName())) {
            document.setDisplayName(fileName.substring(0, fileName.lastIndexOf(CommonConstants.DOT)));
        } else {
            document.setDisplayName(documentsForm.getDisplayName());
        }

        // ファイルデータ
        document.setFileData(getByteFile(documentsForm.getFileData()));

        // 表示順
        if (StringUtils.isEmpty(documentsForm.getDisplayOrder())) {
            document.setDisplayOrder(CommonConstants.MAX_DISPLAY_ORDER);
        } else {
            document.setDisplayOrder(Integer.parseInt(documentsForm.getDisplayOrder()));
        }

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

    // 論理削除処理
    @Transactional
    public void delete(SelectForm selectForm, @AuthenticationPrincipal LoginUserDetails loginUser) {
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < selectForm.getSelectId().size(); i++) {
            idList.add(selectForm.getSelectId().get(i));
        }
        // 複数件排他ロック
        List<Documents> documentList = documentsMapper.forLockByIdList(idList);

        for (int i = 0; i < documentList.size(); i++) {
            // 更新者はセッションの社員番号
            documentList.get(i).setUpdatedBy(loginUser.getLoginUser().getUserNumber());
        }

        // 論理削除
        documentsMapper.delete(documentList);
    }

    // 一覧取得処理
    public List<Documents> findAll() {
        List<Documents> documentList = documentsMapper.findAll();
        return documentList;
    }

    // 1件取得処理
    public Documents findById(int id) {
        Documents documentInfo = documentsMapper.findById(id);
        return documentInfo;
    }

    // ダウンロード処理

}
