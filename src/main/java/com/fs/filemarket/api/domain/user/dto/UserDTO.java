package com.fs.filemarket.api.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// UserSignUpDTO
public class UserDTO {
    private String email;
    private String name;
//    private String login_id;
    private String password;
}
