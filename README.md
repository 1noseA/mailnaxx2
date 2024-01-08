## mailnaxx
社内システム　メールナクス<br>
メールでの連絡・提出をなくし、システム上で完結するようにする

## 目的
- 業務で難しいと感じた権限制御とセッションについて、経験のあるJavaを用いて実際に作成することでイメージを掴む（2022年9月時点）
- 業務で知った様々な機能を自分でも実装してみる

## 使用技術
- Java
- Spring Boot
- PostgreSQL
- MyBatis
- Spring Security
- Thymeleaf
- Lombok

## 画面・機能
※画面デザインは修正予定<br>
<br>
【ログイン】
- テストログイン可能
<img width="1440" alt="スクリーンショット 2024-01-08 15 11 32" src="https://github.com/1noseA/mailnaxx2/assets/59720615/20ab9f28-b943-409d-987a-5c5a63597554">
【メニュー】
<img width="1440" alt="スクリーンショット 2024-01-08 15 12 00" src="https://github.com/1noseA/mailnaxx2/assets/59720615/65416a31-ec00-4de2-b9fd-ea7afd7a6df5">

【社員情報】
- 検索・一覧表示
- 詳細表示
- 登録・削除（総務のみ）
- 更新（自分・総務のみ）
<img width="1440" alt="スクリーンショット 2024-01-08 15 19 46" src="https://github.com/1noseA/mailnaxx2/assets/59720615/b6dd8663-cfac-42c9-a7df-13a46b51bc84">
<img width="1440" alt="スクリーンショット 2024-01-08 15 20 19" src="https://github.com/1noseA/mailnaxx2/assets/59720615/3e260e80-e273-4a49-88eb-7fe9edec3210">

【週報】
- 検索・一覧表示
- 詳細表示
- 一時保存・提出・更新・削除（自分のみ）
- 既読機能（上長のみ）
- 確認・コメント機能（営業のみ）
- 共有機能（営業のみ）
<img width="1440" alt="スクリーンショット 2024-01-08 15 44 38" src="https://github.com/1noseA/mailnaxx2/assets/59720615/52f3e6c1-6241-4ac8-87b8-0b3271cdebed">
<img width="1440" alt="スクリーンショット 2024-01-08 15 30 07" src="https://github.com/1noseA/mailnaxx2/assets/59720615/b30ee926-40b9-4608-8bee-bb8ba4b0107c">

## 今後作成予定
- お知らせ機能
- メール送信機能
- CSVから一括登録
- CSV、Excel出力
- 資料ダウンロード
- API
- バッチ

## 設計書
https://docs.google.com/spreadsheets/d/1vhqNcIKc9IWmd3KZHrJo0bfyUbhAWcKCaqjFE352opM/edit?usp=sharing
