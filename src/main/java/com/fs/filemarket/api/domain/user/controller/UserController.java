package com.fs.filemarket.api.domain.user.controller;

import com.fs.filemarket.api.domain.user.dto.UserDTO;
import com.fs.filemarket.api.domain.user.service.UserService;
import com.mysql.cj.log.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @GetMapping("/")
//    public String Home() {
//        return "home";
//    }
//
//    @GetMapping("/user/join")
//    public String createUserForm() {
//        return "user/createUserForm";
//    }

    @PostMapping("/join")
    public String join(@RequestBody UserDTO userDTO) throws Exception {
        userService.join(userDTO);

        return "회원가입 성공";
    }

    @GetMapping("oauth2/join")
    public String oauth2Join(){

        return "oauth2를 통한 회원가입 성공!!!";
    }


    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }


}