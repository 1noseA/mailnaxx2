package com.mailnaxx2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mailnaxx2.entity.Users;
import com.mailnaxx2.mapper.UsersMapper;
import com.mailnaxx2.model.UsersCsv;
import com.mailnaxx2.values.ProcessClass;

@Service
public class UsersCsvExport {

    @Autowired
    UsersMapper usersMapper;

    // CSVデータ取得処理
    public List<UsersCsv> getUsersCsv(List<Integer> selectUserId) {
        List<Users> userList = usersMapper.findByIdList(selectUserId);
        List<UsersCsv> csvList = new ArrayList<>();
        for (Users user : userList) {
            UsersCsv csvItem = new UsersCsv();
            csvItem.setProcessClass(ProcessClass.NONE.getCode());
            csvItem.setUserNumber(user.getUserNumber());
            csvItem.setUserName(user.getUserName());
            csvItem.setUserNameKana(user.getUserNameKana());
            csvItem.setHireDate(user.getHireDate());
            csvItem.setAffiliation(user.getAffiliation());
            csvItem.setRoleClass(user.getRoleClass());
            csvItem.setSalesFlg(user.getSalesFlg());
            csvItem.setBirthDate(user.getBirthDate());
            csvItem.setPostCode(user.getPostCode());
            csvItem.setAddress(user.getAddress());
            csvItem.setPhoneNumber(user.getPhoneNumber());
            csvItem.setEmailAddress(user.getEmailAddress());
            csvItem.setPassword(user.getPassword());
            csvList.add(csvItem);
        }
        return csvList;
    }

    // CSV出力処理
}
