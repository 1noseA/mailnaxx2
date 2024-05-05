package com.mailnaxx2.dto;

import java.util.ArrayList;
import java.util.List;

import com.mailnaxx2.validation.Message;

import lombok.Data;

/**
 * 社員情報一括登録内容確認DTO
 */
@Data
public class ConfirmFileDTO {

    // 社員情報一括登録DTO
    private List<BulkRegistUsersDTO> userDtoList = new ArrayList<>();

    // エラーメッセージ
    private List<Message> messageList = new ArrayList<>();
}
