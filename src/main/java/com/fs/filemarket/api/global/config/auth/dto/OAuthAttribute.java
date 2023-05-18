package com.fs.filemarket.api.global.config.auth.dto;

import com.fs.filemarket.api.domain.user.Role;
import com.fs.filemarket.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

// OAuth2UserService 를 통해 가져온 OAuth2User 의 att를 담을 클래스
@Getter
public class OAuthAttribute {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttribute(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    // OAuth2User 에서 반환하는 사용자 정보가 Map 이기 때문에 값 하나하나를 변환해야 함
    // userNameAttributeName
    // OAuth2 로그인 진행 시, 키가 되는 필드값
    // 구글의 경우, 기본적으로 코드를 지원함
    public  static OAuthAttribute of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttribute ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // User Entity 생성
    // 생성 시점: 처음 가입 시
    // 가입할 때 기본 권한으로 GUEST 주기 위해, role builder 값으로 Role.GUEST 사용
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .role(Role.GUEST)
                .build();
    }
}
