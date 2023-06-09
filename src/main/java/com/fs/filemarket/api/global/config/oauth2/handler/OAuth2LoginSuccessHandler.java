package com.fs.filemarket.api.global.config.oauth2.handler;

import com.fs.filemarket.api.domain.user.Role;
import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.repository.UserRepository;
import com.fs.filemarket.api.global.config.jwt.service.JwtService;
import com.fs.filemarket.api.global.config.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // 1. 처음 OAuth2 로그인 한 유저
            // User Role == GUEST -> 회원가입 페이지로 리다이렉트
            if(oAuth2User.getRole() == Role.GUEST) {
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);

                jwtService.sendAccessAndRefreshToken(response, accessToken, null);

                // User Role 을 GUEST -> USER 로 업데이트
                try {
                    Optional<User> userOptional = userRepository.findByEmail(oAuth2User.getEmail());
                    User findUser = userOptional.orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));

                    findUser.authorizeUser();
                    userRepository.save(findUser);

                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }

                response.sendRedirect("/"); // 로그인 화면으로 리다이렉트

            } else {
                // 2. 한 번 이상 OAuth2 로그인 했던 유저
                log.info("2. 한 번 이상 OAuth2 로그인 했던 유저");
                loginSuccess(response, oAuth2User);    // 로그인에 성공한 경우 access, refresh 토큰 생성

                response.sendRedirect("/dashboard"); // 메인 홈 화면으로 리다이렉트
            }
        } catch (Exception e) {
            throw e;
        }

    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}