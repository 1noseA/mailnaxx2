package com.mailnaxx2.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.mailnaxx2.entity.Users;
import com.mailnaxx2.form.SearchUsersForm;

@Mapper
public interface UsersMapper {

    // ログイン
    public Optional<Users> findLoginUser(String userNumber);

    // ログイン失敗回数更新
    public void loginFailure(String userNumber);

    // ログイン成功時更新
    public void loginSuccess(String userNumber);

    // 全件取得
    public List<Users> findAll();

    // 検索
    public List<Users> findBySearchForm(SearchUsersForm searchUsersForm);

    // 現場社員取得
    public List<Users> findColleague(String userNumber, int projectId);

    // 1件取得
    public Users findById(int userId);

    // IDを基に社員名取得
    public String findNameById(int userId);

    // 1件排他ロック（ID）
    public Users forLockById(int userId);

    // 1件排他ロック（社員番号）
    public Users forLockByNumber(String userNumber);

    // 複数件排他ロック
    public List<Users> forLockByIdList(List<Integer> idList);

    // 営業担当取得
    public List<Users> findBySales();

    // 登録
    public int insert(Users user);

    // 更新
    public int update(Users user);

    // 論理削除
    public void delete(List<Users> userList);
}
