package com.fs.filemarket.api.global.config.auth;

import com.fs.filemarket.api.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity                 // Spring Security 필터가 스프링 필터체인에 등록됨
@Configuration
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // h2-console 화면을 사용하기 위해 해당 옵션들 disable
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                // authorizeHttpRequests - URL 별 권한 관리를 설정하는 옵션 시작점
                //                       - 이게 선언되어야만 requestMatchers 옵션 사용 가능
                .authorizeHttpRequests()
                // 권한 관리 대상을 지정하는 옵션
                // URL, HTTP 메소드별로 관리 가능
                // "/" 등 지정된 URL 은 permitAll() 옵션으로 전체 열람 권한 부여
                // "/api/v1/**" 주소를 가진 API 는 USER 권한을 가진 사람만 가능
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                // 설정된 값들 이외 나머지 URL 나타냄
                // authenticated()를 추가해 나머지 URL 들은 모두 인증된 사용자(로그인한 사용자)들에게만 허용
                .anyRequest().authenticated()
                .and()
                    // 로그아웃 기능에 대한 여러 설정의 진입점
                    // 로그아웃 성공 시 "/" 주소로 이동
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    // oauth2 로그인 기능에 대한 여러 설정의 진입점
                    .oauth2Login()
                        // oauth2 로그인 성공 이후, 사용자 정보를 가져올 때의 설정들을 담당
                        .userInfoEndpoint()
                    // 소셜 로그인 성공 시, 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
                    // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있음
                    .userService(customOAuth2UserService);

        return http.build();
    }


}
