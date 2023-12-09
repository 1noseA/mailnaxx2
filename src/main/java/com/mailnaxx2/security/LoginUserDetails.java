package com.mailnaxx2.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.mailnaxx2.values.RoleClass;
import com.mailnaxx2.entity.Users;

@SuppressWarnings("serial")
public class LoginUserDetails implements UserDetails {

    private final Users users;

    public LoginUserDetails(Users users) {
        super();
        this.users = users;
    }
    public Users getLoginUser() {
        return users;
    }

    // ハッシュ化済みのパスワードを返す
    @Override
    public String getPassword() {
        return users.getPassword();
    }

    // ログインで利用する社員番号を返す
    @Override
    public String getUsername() {
        return users.getUserNumber();
    }

    // 権限のコレクションを返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_" + RoleClass.getValueByCode(users.getRoleClass()));
    }

    // ユーザーが期限切れでなければtrueを返す
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ユーザーがロックされていなければtrueを返す
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ユーザーのパスワードが期限切れでなければtrueを返す
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ユーザーが有効であればtrueを返す
    @Override
    public boolean isEnabled() {
        return true;
    }
}
