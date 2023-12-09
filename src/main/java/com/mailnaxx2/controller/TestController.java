package com.mailnaxx2.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mailnaxx2.constants.UserConstants;
import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.form.UsersForm;
import com.mailnaxx2.mapper.AffiliationsMapper;
import com.mailnaxx2.mapper.UsersMapper;
import com.mailnaxx2.validation.All;
import com.mailnaxx2.values.RoleClass;

@Controller
public class TestController {

    @Autowired
    AffiliationsMapper affiliationsMapper;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 登録画面初期表示
    @GetMapping("/test/create")
    public String create(@ModelAttribute UsersForm usersForm, Model model) {

        // 所属プルダウン
        List<Affiliations> affiliationList = affiliationsMapper.findAll();
        model.addAttribute("affiliationList", affiliationList);
        model.addAttribute("notAffiliation", UserConstants.NOT_AFFILIATION);

        // 権限区分プルダウン
        model.addAttribute("roleClassList", RoleClass.values());
        return "test/create";
    }

    // 登録画面登録処理
    @PostMapping("/test/create")
    public String create(@ModelAttribute @Validated(All.class) UsersForm usersForm, BindingResult result, Model model) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return create(usersForm, model);
        }

        Users user = new Users();

        // 社員番号生成
        String hireYear = usersForm.getHireYear();
        String hireMonth = usersForm.getHireMonth();
        if (hireMonth.length() == 1) {
            hireMonth = "0" + hireMonth;
        }
        LocalDate hireDate = LocalDate.parse(hireYear + hireMonth + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<Users> usersList =  usersMapper.findAll();
        int max = (int) usersList.stream()
                .filter(u -> u.getHireDate().isEqual(hireDate))
                .count() + 1;
        String num = max >= 10 ? String.valueOf(max) : "0" + String.valueOf(max);
        user.setUserNumber(hireYear + hireMonth + num);

        // 氏名
        user.setUserName(usersForm.getUserLastName() + " " + usersForm.getUserFirstName());
        user.setUserNameKana(usersForm.getUserLastKana() + " " + usersForm.getUserFirstKana());

        // 入社年月
        user.setHireDate(hireDate);

        // 所属
        Affiliations affiliation = new Affiliations();
        affiliation.setAffiliationId(Integer.parseInt(usersForm.getAffiliationId()));
        user.setAffiliation(affiliation);

        // 権限区分
        user.setRoleClass(usersForm.getRoleClass());

        // 生年月日
        String birthYear = usersForm.getBirthYear();
        String birthMonth = usersForm.getBirthMonth();
        String birthDay = usersForm.getBirthDay();
        if (birthMonth.length() == 1) {
            birthMonth = "0" + birthMonth;
        }
        if (birthDay.length() == 1) {
            birthDay = "0" + birthDay;
        }
        user.setBirthDate(LocalDate.parse(birthYear + birthMonth + birthDay, DateTimeFormatter.ofPattern("yyyyMMdd")));

        // 営業担当
        user.setSalesFlg(usersForm.getSalesFlg());

        // 郵便番号
        user.setPostCode(usersForm.getPostCode1() + "-" +usersForm.getPostCode2());

        // 住所
        user.setAddress(usersForm.getAddress());

        // 電話番号
        user.setPhoneNumber(usersForm.getPhoneNumber1() + "-" + usersForm.getPhoneNumber2() + "-" + usersForm.getPhoneNumber3());

        // メールアドレス
        user.setEmailAddress(usersForm.getEmailAddress());

        // パスワードはハッシュにする
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(usersForm.getPassword()));

        // 作成者はセッションのユーザID
        user.setCreatedBy("test");

        usersMapper.insert(user);
        return "login/login";
    }
}
