package com.mailnaxx2.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mailnaxx2.constants.CommonConstants;
import com.mailnaxx2.dto.BulkRegistUsersDTO;
import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.form.BulkRegistUsersForm;
import com.mailnaxx2.validation.Message;
import com.mailnaxx2.values.BulkRegistCsvItem;
import com.mailnaxx2.values.ProcessClass;

@Service
public class ConfirmFileService {

    @Autowired
    AffiliationsService affiliationsService;

    // 入力チェック
    public BulkRegistUsersForm checkFile(MultipartFile file) {
        BulkRegistUsersForm bulkRegistUsersForm = new BulkRegistUsersForm();
        List<Message> messageList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = br.readLine();
            int i = 0;
            while ((line = br.readLine()) != null) {
                i++;
                String[] item = line.split(CommonConstants.COMMA);

                // 必須チェック
                String errorItem = checkRequired(item);
                if (errorItem != null) {
                    Message message = new Message();
                    message.setLineNum(i + "行目");
                    message.setItem(errorItem);
                    message.setContent("入力してください");
                    messageList.add(message);
                }

                // 桁数・文字種チェック
                List<Message> tempList = checkDigits(item);
                if (tempList.size() > 0) {
                    for (Message temp : tempList) {
                        temp.setLineNum(i + "行目");
                    }
                    messageList.addAll(tempList);
                }

                // 処理区分と社員番号の相関チェック
                // checkProcessClass(item);

                // 電話番号整合性
                // checkPhoneNumber(item);

                // メールアドレス整合性
                // checkEmail(item);

                // パスワード整合性
                // checkPassword(item);

            }
            if (messageList.size() > 0) {
                bulkRegistUsersForm.setMessageList(messageList);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bulkRegistUsersForm;
    }

    // 値の設定
    public BulkRegistUsersForm setUserDtoList(MultipartFile file) {
        BulkRegistUsersForm bulkRegistUsersForm = new BulkRegistUsersForm();
        List<BulkRegistUsersDTO> userDtoList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] item = line.split(CommonConstants.COMMA);

                BulkRegistUsersDTO userDto = new BulkRegistUsersDTO();

                // 処理区分
                if (item[0].equals(ProcessClass.INSERT.getCode()) ||
                    item[0].equals(ProcessClass.UPDATE.getCode())) {
                    userDto.setProcessClass(ProcessClass.getViewNameByCode(item[0]));
                } else {
                    // エラー
                }
                // 社員番号
                if (StringUtils.isNotEmpty(item[1])) {
                    userDto.setUserNumber(item[1]);
                }
                // 氏名_漢字
                userDto.setUserName(item[2] + CommonConstants.HALF_SPACE + item[3]);
                // 氏名_カナ
                userDto.setUserNameKana(item[4] + CommonConstants.HALF_SPACE + item[5]);
                // 入社年月
                String hireYear = item[6];
                String hireMonth = item[7];
                if (hireMonth.length() == 1) {
                    hireMonth = CommonConstants.FILLED_ZERO + hireMonth;
                }
                LocalDate hireDate = LocalDate.parse(hireYear + hireMonth + CommonConstants.FIRST_DAY, DateTimeFormatter.ofPattern(CommonConstants.FORMAT_YYMMDD));
                userDto.setHireDate(hireDate);
                // 所属
                Affiliations affiliation = new Affiliations();
                String affiliationName = null;
                if (StringUtils.isEmpty(item[8])) {
                    affiliationName = affiliationsService.findNameById(CommonConstants.DEFAULT_AFFILIATION_ID);
                } else {
                    // 所属IDを基に所属名取得
                    affiliationName = affiliationsService.findNameById(Integer.parseInt(item[8]));
                }
                affiliation.setAffiliationName(affiliationName);
                userDto.setAffiliation(affiliation);
                // 権限区分
                if (StringUtils.isEmpty(item[9])) {
                    userDto.setRoleClass(CommonConstants.DEFAULT_ROLE_CLASS);
                } else {
                    userDto.setRoleClass(item[9]);
                }
                // 営業フラグ
                if (StringUtils.isEmpty(item[10])) {
                    userDto.setSalesFlg(CommonConstants.DEFAULT_SALES_FLG);
                } else {
                    userDto.setSalesFlg(item[10]);
                }
                // 生年月日
                String birthYear = item[11];
                String birthMonth = item[12];
                String birthDay = item[13];
                if (birthMonth.length() == 1) {
                    birthMonth = CommonConstants.FILLED_ZERO + birthMonth;
                }
                if (birthDay.length() == 1) {
                    birthDay = CommonConstants.FILLED_ZERO + birthDay;
                }
                userDto.setBirthDate(LocalDate.parse(birthYear + birthMonth + birthDay, DateTimeFormatter.ofPattern(CommonConstants.FORMAT_YYMMDD)));
                // 郵便番号
                userDto.setPostCode(item[14]+ CommonConstants.HALF_HYPHEN  + item[15]);
                // 住所
                userDto.setAddress(item[16]);
                // 電話番号
                userDto.setPhoneNumber(item[17] + CommonConstants.HALF_HYPHEN + item[18]+ CommonConstants.HALF_HYPHEN + item[19]);
                // メールアドレス
                userDto.setEmailAddress(item[20]);
                // パスワード
                userDto.setPassword(item[21]);
                userDtoList.add(userDto);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bulkRegistUsersForm.setUserDtoList(userDtoList);
        return bulkRegistUsersForm;
    }

    // 必須チェック
    private String checkRequired(String[] item) {
        String errorItem = null;
        String itemName = null;
        // 必須チェックが必要な項目
        List<Integer> targetNum = List.of(0, 2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21);
        for (int i = 0; i < item.length; i++) {
            // 未入力の場合、項目番号をListに追加
            if (targetNum.contains(i) && StringUtils.isEmpty(item[i])) {
                itemName = BulkRegistCsvItem.getViewNameByCode(String.valueOf(i));
                errorItem = (errorItem == null) ? itemName : errorItem + CommonConstants.TOTEN + itemName;
            }
        }
        return errorItem;
    }

    // 桁数・文字種チェック
    private List<Message> checkDigits(String[] item) {
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < item.length; i++) {
            if (StringUtils.isEmpty(item[i])) {
                break;
            }
            switch (i) {
            case 2, 3:
                if (!item[i].matches("^[^ -~｡-ﾟ]{1,10}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("全角10文字以内で入力してください");
                    messageList.add(message);
                }
                break;
            case 4, 5:
                if (!item[i].matches("^[ァ-ヶー]{1,10}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("全角カタカナ10文字以内で入力してください");
                    messageList.add(message);
                }
                break;
            case 6, 11, 15:
                if (!item[i].matches("^[0-9]{4}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("半角数字4桁で入力してください");
                    messageList.add(message);
                }
                break;
            case 7, 12, 13:
                if (!item[i].matches("^[0-9]{2}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("半角数字2桁で入力してください");
                    messageList.add(message);
                }
                break;
            case 14:
                if (!item[i].matches("^[0-9]{3}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("半角数字3桁で入力してください");
                    messageList.add(message);
                }
                break;
            case 16:
                if (!item[i].matches("^[^ -~｡-ﾟ]{1,256}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("全角256文字以内で入力してください");
                    messageList.add(message);
                }
                break;
            case 17:
                if (!item[i].matches("^[0-9]{1,5}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("半角数字5桁以内で入力してください");
                    messageList.add(message);
                }
                break;
            case 18, 19:
                if (!item[i].matches("^[0-9]{1,4}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("半角数字4桁以内で入力してください");
                    messageList.add(message);
                }
                break;
            case 20:
                if (!item[i].matches("^[a-zA-Z0-9]{1,128}$")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("半角英数字128文字以内で入力してください");
                    messageList.add(message);
                }
                break;
            case 21:
                if (!item[i].matches("/^(?=.*?[0-9])[a-zA-Z0-9]{8,10}$/")) {
                    Message message = new Message();
                    message.setItem(BulkRegistCsvItem.getViewNameByCode(String.valueOf(i)));
                    message.setContent("半角英数字8文字以上10文字以内で入力してください");
                    messageList.add(message);
                }
                break;
            }
        }
        return messageList;
    }

    // 処理区分と社員番号の相関チェック

    // 電話番号整合性

    // メールアドレス整合性

    // パスワード整合性

}
