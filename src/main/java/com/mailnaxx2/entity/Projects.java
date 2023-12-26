package com.mailnaxx2.entity;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 現場
 */
@Data
public class Projects {

    // 現場ID
    private int projectId;

    // 会社名
    private String companyName;

    // 案件名
    private String projectName;

    // 担当営業
    private Users salesUser;

    // 削除フラグ
    private String deletedFlg;

    // レコード登録者
    private String createdBy;

    // レコード登録日
    private LocalDateTime createdAt;

    // レコード更新者
    private String updatedBy;

    // レコード更新日
    private LocalDateTime updatedAt;
}
