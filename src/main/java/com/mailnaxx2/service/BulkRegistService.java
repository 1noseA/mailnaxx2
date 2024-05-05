package com.mailnaxx2.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mailnaxx2.constants.CommonConstants;
import com.mailnaxx2.dto.BulkRegistUsersDTO;
import com.mailnaxx2.dto.CompletedBulkRegistDTO;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.mapper.UsersMapper;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.values.ProcessClass;

@Service
public class BulkRegistService {

    @Autowired
    UsersMapper usersMapper;

    // 一括登録処理
    @Transactional
    public CompletedBulkRegistDTO insert(List<BulkRegistUsersDTO> userDtoList, @AuthenticationPrincipal LoginUserDetails loginUser) {

        int insertCount = 0;
        int updateCount = 0;

        CompletedBulkRegistDTO completedDTO = new CompletedBulkRegistDTO();
        for (int i = 0; i < userDtoList.size(); i++) {
            BulkRegistUsersDTO dto = userDtoList.get(i);
            Users user = new Users();

            // 更新の場合、排他ロックを行う
            if (dto.getProcessClass().equals(ProcessClass.UPDATE.getCode())) {
                user = usersMapper.forLockByNumber(dto.getUserNumber());
            }
            // エンティティにセットする
            user = setEntity(user, dto);

            if (dto.getProcessClass().equals(ProcessClass.INSERT.getCode())) {
                // パスワードはハッシュにする
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                // 作成者はセッションの社員番号
                user.setCreatedBy(loginUser.getLoginUser().getUserNumber());

                // 登録
                usersMapper.insert(user);
                insertCount++;
            } else { // 登録の場合
                // パスワードは入力されていたら変更
                if (dto.getPassword() != "") {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    // パスワード変更日時
                    user.setPassChangedDate(LocalDateTime.now());
                    // 前回パスワード
                    user.setOldPassword(user.getPassword());
                }
                // 更新者はセッションの社員番号
                user.setUpdatedBy(loginUser.getLoginUser().getUserNumber());

                // 更新
                usersMapper.update(user);
                updateCount++;
            }
        }
        completedDTO.setInsertCount(insertCount);
        completedDTO.setUpdateCount(updateCount);
        return completedDTO;
    }

    // エンティティにセットする
    private Users setEntity(Users user, BulkRegistUsersDTO dto) {
        // 社員番号
        if (dto.getProcessClass().equals(ProcessClass.UPDATE.getCode())) {
            user.setUserNumber(dto.getUserNameKana());
        } else {
            // 社員番号生成
            List<Users> usersList =  usersMapper.findAll();
            int max = (int) usersList.stream()
                    .filter(u -> u.getHireDate().isEqual(dto.getHireDate()))
                    .count() + 1;
            String num = max >= 10 ? String.valueOf(max) : CommonConstants.FILLED_ZERO + String.valueOf(max);
            user.setUserNumber(dto.getHireDate() + num);
        }

        // 氏名
        user.setUserName(dto.getUserName());
        user.setUserNameKana(dto.getUserNameKana());

        // 入社年月
        user.setHireDate(dto.getHireDate());

        // 所属
        user.setAffiliation(dto.getAffiliation());

        // 権限区分
        user.setRoleClass(dto.getRoleClass());

        // 生年月日
        user.setBirthDate(dto.getBirthDate());

        // 営業担当
        user.setSalesFlg(dto.getSalesFlg());

        // 郵便番号
        user.setPostCode(dto.getPostCode());

        // 住所
        user.setAddress(dto.getAddress());

        // 電話番号
        user.setPhoneNumber(dto.getPhoneNumber() );

        // メールアドレス
        user.setEmailAddress(dto.getEmailAddress());

        return user;
    }
}
