package com.fs.filemarket.api.global.config.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs.filemarket.api.domain.user.repository.UserRepository;
import com.fs.filemarket.api.global.config.jwt.filter.JwtAuthenticationProcessingFilter;
import com.fs.filemarket.api.global.config.jwt.service.JwtService;
import com.fs.filemarket.api.global.config.login.filter.CustomUsernamePasswordAuthenticationFilter;
import com.fs.filemarket.api.global.config.login.handler.LoginFailureHandler;
import com.fs.filemarket.api.global.config.login.handler.LoginSuccessHandler;
import com.fs.filemarket.api.global.config.login.service.LoginService;
import com.fs.filemarket.api.global.config.oauth2.handler.OAuth2LoginFailureHandler;
import com.fs.filemarket.api.global.config.oauth2.handler.OAuth2LoginSuccessHandler;
import com.fs.filemarket.api.global.config.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity                 // Spring Security 필터가 스프링 필터체인에 등록됨
public class SecurityConfig{

    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable() // FormLogin 사용 X
                .httpBasic().disable() // httpBasic 사용 X
                .csrf().disable() // csrf 보안 사용 X
                .headers().frameOptions().disable()
                .and()

                // 세션 사용하지 않으므로 STATELESS 로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable()
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/").permitAll()
                );

        http
                // authorizeHttpRequests - URL 별 권한 관리를 설정하는 옵션 시작점
                //                       - 이게 선언되어야만 requestMatchers 옵션 사용 가능
                .authorizeHttpRequests()
//                // 권한 관리 대상을 지정하는 옵션
//                // URL, HTTP 메소드별로 관리 가능
//                // 아이콘, css, js 관련
//                // 기본 페이지, css, image 하위 폴더에 있는 자료들은 모두 접근 가능
//                .requestMatchers("/","/user/**","/folders/**","/files/**").permitAll()
//                .requestMatchers("/join").permitAll()    // 회원가입 접근 가능
//                // 설정된 값들 이외 나머지 URL 들은 모두 인증된 사용자(로그인한 사용자)들에게만 허용
//                .anyRequest().authenticated()

                .anyRequest().permitAll()


                // OAuth2 로그인
                .and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint()                         // oauth2 로그인 성공 이후, 사용자 정보를 가져올 때의 설정들을 담당
                .userService(customOAuth2UserService);      // 소셜 로그인 성공 시, 후속 조치를 진행할 UserService 인터페이스로 매핑됨

        // 일반 로그인
        http.addFilterAfter(customUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     *
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordLoginFilter
                = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        customUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationFilter;
    }



}
