package com.mailnaxx2.form;

import java.util.List;

import com.mailnaxx2.dto.BulkRegistUsersDTO;
import com.mailnaxx2.validation.Message;

import lombok.Data;

/**
 * 社員情報一括登録
 */
@Data
public class BulkRegistUsersForm {

    // 社員情報一括登録DTO
    private List<BulkRegistUsersDTO> userDtoList;

    // エラーメッセージ
    private List<Message> messageList;
}
