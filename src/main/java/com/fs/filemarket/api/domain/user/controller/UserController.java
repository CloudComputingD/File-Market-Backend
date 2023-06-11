package com.fs.filemarket.api.domain.user.controller;

import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.domain.user.UserInfo;
import com.fs.filemarket.api.domain.user.dto.UserDTO;
import com.fs.filemarket.api.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "User", description = "User Controller")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // 서비스 첫 화면

    @Operation(summary = "로그인 화면을 요청합니다..")
    @GetMapping("/")
    public String signIn() {
        return "signIn";
    }

    /** 회원가입 **/

    @Operation(summary = "회원가입화면을 요청합니다..")
    // 회원가입(sign-up) 화면 요청
    @GetMapping("/signup")
    public String signupForm() {
        return "signupForm";
    }

    @Operation(summary = "대시보드 화면을 요청합니다..")
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // 자체 회원가입
    @Operation(summary = "자체회원가입을 진행합니다.")
    @PostMapping("/signup")
    public String signup(@RequestBody UserDTO userDTO) throws Exception {
        userService.join(userDTO);

        return "회원가입 성공";  // return "redirect:/index"
    }

    // oauth2 를 통한 회원가입
    @Operation(summary = "oauth2를 통한 회원가입 화면을 요청합니다.")
    @GetMapping("oauth2/signup")
    public String oauth2Signup(){

        return "oauth2를 통한 회원가입 성공!!!";
    }


    /** 로그인 **/
    // 자체 로그인 path -> /login + POST 로 온 요청에 매핑
    // oauth 로그인 path -> /oauth2/authorization/google 매핑


    // 유저 정보 반환
    @Operation(summary = "현재 유저 정보를 반환합니다.")
    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        UserDTO userDTO = new UserDTO();

        userDTO.setId(currentUser.getId());
        userDTO.setName(currentUser.getName());
        userDTO.setEmail(currentUser.getEmail());
        userDTO.setRole(currentUser.getRole());

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/info")
    public Optional<UserInfo> userInfo(Model model, @RequestParam String email) {
        Optional<User> user = userService.userInfo(email);

        Optional<UserInfo> userInfo = user.map(u -> new UserInfo(u. getId(), u.getName(), u.getEmail(), u.getRole()));

        model.addAttribute("userInfo", userInfo);
        return userInfo;
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

}