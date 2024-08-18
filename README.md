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
<img width="1440" alt="スクリーンショット 2024-08-18 15 56 14" src="https://github.com/user-attachments/assets/eb654fcc-da33-4f03-b9b2-04b93f7f2a3d">

【社員情報】
- 検索・一覧表示
- 詳細表示
- 登録・削除（総務のみ）
- 更新（自分・総務のみ）
- CSVから一括登録
- CSV出力
<img width="1440" alt="スクリーンショット 2024-08-18 16 15 39" src="https://github.com/user-attachments/assets/37f20254-bced-45bf-929e-8071ad79bc73">
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

【マニュアル（API）】<br>
https://github.com/1noseA/manual-api<br>
- 一覧表示
- 詳細表示
- 登録・更新・削除
<img width="1440" alt="スクリーンショット 2024-08-18 16 05 30" src="https://github.com/user-attachments/assets/d2a701b4-0943-491c-bc62-27e36b5c89b6">

【資料ダウンロード】
- 削除
- 登録
<img width="1440" alt="スクリーンショット 2024-08-18 16 03 40" src="https://github.com/user-attachments/assets/fcca7c70-cae8-4054-8011-bd9937550d7d">
<img width="1440" alt="スクリーンショット 2024-08-18 16 04 07" src="https://github.com/user-attachments/assets/ac0dcae1-2084-461b-9ea3-ffdca61701a1">

【お知らせ】
- 一覧表示
- 詳細表示
- 登録・更新・削除
<img width="1440" alt="スクリーンショット 2024-08-18 16 12 43" src="https://github.com/user-attachments/assets/806a2faf-9e65-450b-adc1-eb5f6417c63f">
<img width="1440" alt="スクリーンショット 2024-08-18 15 57 06" src="https://github.com/user-attachments/assets/9d5b43f7-cb3a-48af-83dc-64462ace9cdd">

## 今後作成予定
- 一括削除バッチ

## 設計書
https://docs.google.com/spreadsheets/d/1vhqNcIKc9IWmd3KZHrJo0bfyUbhAWcKCaqjFE352opM/edit?usp=sharing
