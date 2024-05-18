package com.mailnaxx2.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mailnaxx2.constants.CommonConstants;
import com.mailnaxx2.constants.UserConstants;
import com.mailnaxx2.dto.BulkRegistUsersDTO;
import com.mailnaxx2.dto.CompletedBulkRegistDTO;
import com.mailnaxx2.dto.ConfirmFileDTO;
import com.mailnaxx2.entity.Affiliations;
import com.mailnaxx2.entity.Users;
import com.mailnaxx2.form.SearchUsersForm;
import com.mailnaxx2.form.SelectForm;
import com.mailnaxx2.form.UsersForm;
import com.mailnaxx2.model.UsersCsv;
import com.mailnaxx2.security.LoginUserDetails;
import com.mailnaxx2.service.AffiliationsService;
import com.mailnaxx2.service.BulkRegistService;
import com.mailnaxx2.service.ConfirmFileService;
import com.mailnaxx2.service.UsersCsvExport;
import com.mailnaxx2.service.UsersService;
import com.mailnaxx2.validation.All;
import com.mailnaxx2.validation.GroupOrder;
import com.mailnaxx2.validation.Message;
import com.mailnaxx2.values.ProcessClass;
import com.mailnaxx2.values.RoleClass;

@Controller
public class UsersController {

    @Autowired
    HttpSession session;

    @Autowired
    UsersService usersService;

    @Autowired
    AffiliationsService affiliationsService;

    @Autowired
    ConfirmFileService confirmFileService;

    @Autowired
    BulkRegistService bulkRegistService;

    @Autowired
    UsersCsvExport usersCsvExport;

    // 管理者
    boolean isAdmin;

    // 社員一覧
    List<Users> userList;

    // 所属プルダウン
    List<Affiliations> affiliationList;

     // 社員詳細
    Users userInfo;

    // 一覧画面初期表示
    @RequestMapping("/user/list")
    public String index(SearchUsersForm searchUsersForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 社員一覧を取得
        userList = usersService.findAll();
        model.addAttribute("userList", userList);
        model.addAttribute("roleClassList", RoleClass.values());

        searchUsersForm.setSearchCondition(CommonConstants.PREFIX_MATCH);
        model.addAttribute("searchUsersForm", searchUsersForm);

        // 権限（総務のみ）
        isAdmin = false;
        if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            isAdmin = true;
        }
        session.setAttribute("session_isAdmin", isAdmin);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/list";
    }

    // 検索処理
    @PostMapping("/user/search")
    public String search(SearchUsersForm searchUsersForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 社員一覧を取得
        userList = usersService.findBySearchForm(searchUsersForm);
        model.addAttribute("userList", userList);
        model.addAttribute("roleClassList", RoleClass.values());

        // 権限
        isAdmin = (boolean) session.getAttribute("session_isAdmin");
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/list";
    }

    // 一括登録初期表示
    @GetMapping("/user/upload-file")
    public String uploadFile(Model model,
                             @AuthenticationPrincipal LoginUserDetails loginUser) {
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/upload-file";
    }

    // 内容確認
    @PostMapping("/user/confirm-file")
    public String confirmFile(@RequestParam("file") MultipartFile file,
                              Model model,
                              @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力チェック
        ConfirmFileDTO dto = confirmFileService.checkFile(file);
        // エラーの場合
        if (dto.getMessageList().size() > 0) {
            model.addAttribute("messageList", dto.getMessageList());
            return uploadFile(model, loginUser);
        }

        // 値の設定
        dto = confirmFileService.setUserDtoList(file);
        session.setAttribute("session_userDtoList", dto.getUserDtoList());
        model.addAttribute("userDtoList", dto.getUserDtoList());
        model.addAttribute("processClassList", ProcessClass.values());
        model.addAttribute("roleClassList", RoleClass.values());
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/confirm-file";
    }

    // 一括登録処理
    @PostMapping("/user/bulk-regist")
    public String bulkRegist(Model model,
                             @AuthenticationPrincipal LoginUserDetails loginUser) {
        @SuppressWarnings({ "unchecked", "unused" })
        List<BulkRegistUsersDTO> userDtoList = (List<BulkRegistUsersDTO>) session.getAttribute("session_userDtoList");
        // 一括登録処理
        CompletedBulkRegistDTO completedDTO = bulkRegistService.insert(userDtoList, loginUser);
        // エラーの場合
        if (completedDTO.getErrorCount() > 0) {
            Message message = new Message();
            message.setContent("登録・更新に失敗しました");
            List<Message> messageList = new ArrayList<>();
            messageList.add(message);
            model.addAttribute("messageList", messageList);
            return uploadFile(model, loginUser);
        }

        model.addAttribute("insertCount", completedDTO.getInsertCount());
        model.addAttribute("updateCount", completedDTO.getUpdateCount());
        model.addAttribute("errorCount", completedDTO.getErrorCount());
        model.addAttribute("totalCount", completedDTO.getTotalCount());
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/bulk-regist";
    }

    // 登録画面初期表示
    @GetMapping("/user/create")
    public String create(@ModelAttribute UsersForm usersForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        model.addAttribute("userId", 0);

        // 所属プルダウン
        affiliationList = affiliationsService.findAll();
        model.addAttribute("affiliationList", affiliationList);
        model.addAttribute("notAffiliation", UserConstants.NOT_AFFILIATION);

        // 権限区分プルダウン
        model.addAttribute("roleClassList", RoleClass.values());

        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/create";
    }

    // 登録処理
    @PostMapping("/user/create")
    public String create(@ModelAttribute @Validated(All.class) UsersForm usersForm,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            // リダイレクトだと入力エラーの値が引き継がれない
            // return "redirect:/user/create";
            return create(usersForm, model, loginUser);
        }

        // 登録サービス実行
        usersService.insert(usersForm, loginUser);

        return "redirect:/user/list";
    }

    // CSV出力
    @PostMapping("/user/csv-export")
    public String csvExport(@ModelAttribute SelectForm selectForm,
                            SearchUsersForm searchUsersForm,
                            Model model,
                            @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 選択がなかったら一覧表示全件出力
        if (selectForm.getSelectUserId() == null) {
            return  index(searchUsersForm, model, loginUser);
        }
        // CSVデータ取得処理
        List<UsersCsv> csvList = usersCsvExport.getUsersCsv(selectForm.getSelectUserId());

        // CSV出力処理

        return index(searchUsersForm, model, loginUser);
    }

    // 論理削除処理
    @PostMapping("/user/delete")
    public String delete(@ModelAttribute SelectForm selectForm,
                        SearchUsersForm searchUsersForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力チェック
        if (selectForm.getSelectUserId() == null) {
            // エラーメッセージを表示
            model.addAttribute("message", "対象を選択してください。");
            return index(searchUsersForm, model, loginUser);
        }
        for (int selectUser : selectForm.getSelectUserId()) {
            if (selectUser == loginUser.getLoginUser().getUserId()) {
                // エラーメッセージを表示
                model.addAttribute("message", "自分自身は削除できません。");
                return index(searchUsersForm, model, loginUser);
            }
        }

        // 権限チェック
        if (loginUser.getLoginUser().getRoleClass().equals(RoleClass.AFFAIRS.getCode())) {
            usersService.delete(selectForm, loginUser);
            return "redirect:/user/list";
        } else {
            // エラーメッセージを表示
            model.addAttribute("message", "権限がありません。");
            return index(searchUsersForm, model, loginUser);
        }
    }

    // 詳細画面初期表示
    @PostMapping("/user/detail")
    public String detail(int userId,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        userInfo = usersService.findById(userId);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("roleClass", RoleClass.getViewNameByCode(userInfo.getRoleClass()));
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/detail";
    }

    // 編集画面初期表示
    @PostMapping("/user/edit")
    public String edit(int userId,
                        @ModelAttribute UsersForm usersForm,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 詳細情報を取得
        userInfo = usersService.findById(userId);
        // 入力フォームに設定
        setInputForm(userInfo, usersForm);

        // 所属プルダウン
        affiliationList = affiliationsService.findAll();
        model.addAttribute("affiliationList", affiliationList);
        model.addAttribute("notAffiliation", UserConstants.NOT_AFFILIATION);

        // 権限区分プルダウン
        model.addAttribute("roleClassList", RoleClass.values());

        model.addAttribute("userId", userId);
        model.addAttribute("roleClass", RoleClass.getViewNameByCode(userInfo.getRoleClass()));
        model.addAttribute("loginUserInfo", loginUser.getLoginUser());
        return "user/create";
    }

    // 更新処理
    @Transactional
    @PostMapping("/user/update")
    public String update(int userId,
                        @ModelAttribute @Validated(GroupOrder.class) UsersForm usersForm,
                        BindingResult result,
                        Model model,
                        @AuthenticationPrincipal LoginUserDetails loginUser) {
        // 入力エラーチェック
        if (result.hasErrors()) {
            return edit(userId, usersForm, model, loginUser);
        }

        Users user = new Users();
        user.setUserId(userId);

        // 更新サービス実行
        usersService.update(user, usersForm, loginUser);

        return "redirect:/user/list";
    }

    // 入力フォームに設定
    private void setInputForm(Users userInfo, UsersForm usersForm) {
        String[] userName = userInfo.getUserName().split(CommonConstants.HALF_SPACE);
        usersForm.setUserLastName(userName[0]);
        usersForm.setUserFirstName(userName[1]);
        String[] userNameKana = userInfo.getUserNameKana().split(CommonConstants.HALF_SPACE);
        usersForm.setUserLastKana(userNameKana[0]);
        usersForm.setUserFirstKana(userNameKana[1]);
        String[] hireDate = userInfo.getHireDate().toString().split(CommonConstants.HALF_HYPHEN);
        usersForm.setHireYear(hireDate[0]);
        usersForm.setHireMonth(hireDate[1].replaceFirst("^0+", ""));
        usersForm.setAffiliationId(String.valueOf(userInfo.getAffiliation().getAffiliationId()));
        usersForm.setRoleClass(userInfo.getRoleClass());
        usersForm.setSalesFlg(userInfo.getSalesFlg());
        String[] birthDate = userInfo.getBirthDate().toString().split(CommonConstants.HALF_HYPHEN);
        usersForm.setBirthYear(birthDate[0]);
        usersForm.setBirthMonth(birthDate[1].replaceFirst("^0+", ""));
        usersForm.setBirthDay(birthDate[2].replaceFirst("^0+", ""));
        String[] postCode = userInfo.getPostCode().split(CommonConstants.HALF_HYPHEN);
        usersForm.setPostCode1(postCode[0]);
        usersForm.setPostCode2(postCode[1]);
        usersForm.setAddress(userInfo.getAddress());
        String[] phoneNumber = userInfo.getPhoneNumber().split(CommonConstants.HALF_HYPHEN);
        usersForm.setPhoneNumber1(phoneNumber[0]);
        usersForm.setPhoneNumber2(phoneNumber[1]);
        usersForm.setPhoneNumber3(phoneNumber[2]);
        usersForm.setEmailAddress(userInfo.getEmailAddress());
    }
}
