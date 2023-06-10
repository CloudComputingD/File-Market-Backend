package com.fs.filemarket.api.domain.user.dto;

import com.fs.filemarket.api.domain.user.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class UserDTO {
    private Integer id;
    private String email;
    private String name;
    private String password;
    private Role role;
}
