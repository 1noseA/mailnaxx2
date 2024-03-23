package com.mailnaxx2.jackson;

import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Manuals {

    // マニュアルID
    public int manualId;

    // 社員ID
    public int userId;

    // 表示順
    public int displayOrder;

    // タイトル
    public String title;

    // 掲載開始日
    public LocalDate startDate;

    // 掲載終了日
    private LocalDate endDate;

    // 内容
    public String content;

    // リンク
    public String link;

    // 引数なしのコンストラクタを定義しないとデシリアライズ（JSON -> Javaオブジェクトへの変換）時にエラーが起きる
    public Manuals() {
    }

    @Override
    public String toString() {
        return "Manuals{" +
                "manualId='" + manualId +
                ", userId=" + userId +
                ", displayOrder=" + displayOrder +
                ", title=" + title +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", content=" + content +
                ", link=" + link +
                '}';
    }
}
