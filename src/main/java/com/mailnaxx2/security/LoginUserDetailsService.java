package com.mailnaxx2.security;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mailnaxx2.mapper.UsersMapper;
import com.mailnaxx2.entity.Users;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    private final UsersMapper usersMapper;

    public LoginUserDetailsService(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String number) throws UsernameNotFoundException {
        Optional<Users> userOp = usersMapper.findLoginUser(number);
        return userOp.map(users -> new LoginUserDetails(users))
                .orElseThrow(() -> new UsernameNotFoundException("ログインに失敗しました。"));
    }

    // ログイン失敗時のハンドラ
    @EventListener
    public void loginFailureHandle(AuthenticationFailureBadCredentialsEvent event) {
        String userNumber = event.getAuthentication().getName();
        usersMapper.loginFailure(userNumber);
    }

    // ログイン成功時のハンドラ
    @EventListener
    public void loginSuccessHandle(AuthenticationSuccessEvent event) {
        String userNumber = event.getAuthentication().getName();
        // 最終ログイン日時の更新とログイン失敗回数初期化
        usersMapper.loginSuccess(userNumber);

        // セキュリティコンテキストの内容を更新
        Optional<Users> user = usersMapper.findLoginUser(userNumber);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
