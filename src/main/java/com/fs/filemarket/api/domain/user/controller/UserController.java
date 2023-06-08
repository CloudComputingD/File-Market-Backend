package com.fs.filemarket.api.domain.user.controller;

import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.dto.UserDTO;
import com.fs.filemarket.api.domain.user.service.UserService;
import com.mysql.cj.log.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // 서비스 첫 화면

//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }

    /** 회원가입 **/

    // 회원가입(sign-up) 화면 요청
    @GetMapping("/signup")
    public String signupForm() {
        return "signupForm";
    }

    // 자체 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody UserDTO userDTO) throws Exception {
        userService.join(userDTO);

        return "회원가입 성공";  // return "redirect:/index"
    }

    // oauth2 를 통한 회원가입
    @GetMapping("oauth2/signup")
    public String oauth2Signup(){

        return "oauth2를 통한 회원가입 성공!!!";
    }


    /** 로그인 **/
    // 자체 로그인 path -> /login + POST 로 온 요청에 매핑
    // oauth 로그인 path -> /oauth2/authorization/google 매핑


    // 유저 정보 반환
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }


    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }


}