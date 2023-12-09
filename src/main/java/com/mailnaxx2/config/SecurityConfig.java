package com.mailnaxx2.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ログイン設定
        http.formLogin(login -> login
                .usernameParameter("userNumber")
                .passwordParameter("password")
                // 入力値送信先URL
                .loginProcessingUrl("/login")
                // ログイン画面のURL
                .loginPage("/")
                // ログイン成功後のリダイレクト先URL
                .defaultSuccessUrl("/top")
                // ログイン失敗後のリダイレクト先URL
                .failureUrl("/")
                // ログイン画面はログインなしでもアクセス可
                .permitAll()
        // ログアウト設定
        ).logout(logout -> logout
                // ログアウト成功後の遷移先
                .logoutSuccessUrl("/")
        // URLごとの認可設定
        ).authorizeHttpRequests(authz -> authz
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // ROLE_ADMINのみアクセス可
                .requestMatchers("/admin").hasRole("ADMIN")
                // アクセス許可
                .requestMatchers("/loginCheck", "/testLogin", "/test/create").permitAll()
                // 他のURLはログイン後のみ
                .anyRequest().authenticated()
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
