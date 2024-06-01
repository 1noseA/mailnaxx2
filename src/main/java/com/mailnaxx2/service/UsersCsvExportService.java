package com.mailnaxx2.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mailnaxx2.constants.UserConstants;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.mapper.UsersMapper;
import com.mailnaxx2.model.UsersCsv;
import com.mailnaxx2.values.ProcessClass;

@Service
public class UsersCsvExportService {

    @Autowired
    UsersMapper usersMapper;

    // 社員情報取得
    public List<Users> findByIdList(List<Integer> selectUserId) {
        List<Users> userList = usersMapper.findByIdList(selectUserId);
        return userList;
    }

    // CSV項目設定
    public List<UsersCsv> setUsersCsv(List<Users> userList) {
        // 最大10000件までで切り取る
        int maxLine = UserConstants.CSV_MAX_LINE;
        if (userList.size() > maxLine) {
            userList = new ArrayList<>(userList.subList(0, maxLine));
        }

        List<UsersCsv> csvList = new ArrayList<>();
        for (Users user : userList) {
            UsersCsv csv = new UsersCsv();
            csv.setProcessClass(ProcessClass.NONE.getCode());
            csv.setUserNumber(user.getUserNumber());
            csv.setUserName(user.getUserName());
            csv.setUserNameKana(user.getUserNameKana());
            csv.setHireDate((user.getHireDate()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            csv.setAffiliationId(String.valueOf(user.getAffiliation().getAffiliationId()));
            csv.setRoleClass(user.getRoleClass());
            csv.setSalesFlg(user.getSalesFlg());
            csv.setBirthDate((user.getBirthDate()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            csv.setPostCode(user.getPostCode());
            csv.setAddress(user.getAddress());
            csv.setPhoneNumber(user.getPhoneNumber());
            csv.setEmailAddress(user.getEmailAddress());
            csv.setPassword("********");
            csvList.add(csv);
        }
        return csvList;
    }
}
