package com.mailnaxx2.dto;

import lombok.Data;

/**
 * 社員情報一括登録完了DTO
 */
@Data
public class CompletedBulkRegistDTO {

    // 登録件数
    private int insertCount;

    // 更新件数
    private int updateCount;
}
