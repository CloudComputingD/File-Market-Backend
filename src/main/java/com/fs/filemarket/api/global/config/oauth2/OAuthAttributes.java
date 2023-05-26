package com.fs.filemarket.api.global.config.oauth2;

import com.fs.filemarket.api.domain.user.Role;
import com.fs.filemarket.api.domain.user.SocialType;
import com.fs.filemarket.api.domain.user.User;
import com.fs.filemarket.api.global.config.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.fs.filemarket.api.global.config.oauth2.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

// OAuth2UserService 를 통해 가져온 OAuth2User 의 att를 담을 클래스
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;


    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    // OAuth2User 에서 반환하는 사용자 정보가 Map 이기 때문에 값 하나하나를 변환해야 함
    // userNameAttributeName
    // OAuth2 로그인 진행 시, 키가 되는 필드값
    // 구글의 경우, 기본적으로 코드를 지원함
    public  static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    // User Entity 생성
    // of 메서드로 OAuthAtt 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo 가 소셜 타입별로 주입된 상태
    // OAuth2UserInfo 에서 socialId(식별값), name 을 가져와서 build
    // email 에는 UUID 로 중복 없는 랜덤 값 생성
    // 가입할 때 기본 권한으로 GUEST 주기 위해, role builder 값으로 Role.GUEST 사용
    public User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .name(oAuth2UserInfo.getName())
                .role(Role.GUEST)
                .build();
    }
}
