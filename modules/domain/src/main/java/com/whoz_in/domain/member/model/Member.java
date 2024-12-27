package com.whoz_in.domain.member.model;

import com.whoz_in.domain.member.exception.NotAuthMemberException;
import com.whoz_in.domain.member.service.PasswordEncoder;
import com.whoz_in.domain.shared.AggregateRoot;
import com.whoz_in.domain.shared.Nullable;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Member extends AggregateRoot {
    @Getter private final MemberId id;
    @Getter private final String name; //실명
    @Getter private final Position mainPosition;
    @Getter private final int generation; //기수
    @Getter private String statusMessage; //상태 메세지
    @Nullable private AuthCredentials authCredentials;
    @Nullable private OAuthCredentials oAuthCredentials;

    public Optional<AuthCredentials> getAuthCredentials(){
        return Optional.ofNullable(authCredentials);
    }
    public Optional<OAuthCredentials> getOAuthCredentials(){
        return Optional.ofNullable(oAuthCredentials);
    }

    //일반 회원가입
    public static Member create(String name, Position mainPosition, int generation, AuthCredentials authCredentials){
        return create(name, mainPosition, generation, authCredentials, null);
    }
    //소셜 회원가입
    public static Member create(String name, Position mainPosition, int generation, OAuthCredentials oAuthCredentials){
        return create(name, mainPosition, generation, null, oAuthCredentials);
    }
    private static Member create(String name, Position mainPosition, int generation,
            AuthCredentials authCredentials, OAuthCredentials oAuthCredentials){
        if (authCredentials == null && oAuthCredentials == null)
            throw new IllegalStateException("no auth and oauth");
        return builder()
                .id(new MemberId())
                .name(name)
                .mainPosition(mainPosition)
                .generation(generation)
                .statusMessage("")
                .authCredentials(authCredentials)
                .oAuthCredentials(oAuthCredentials)
                .build();
    }

    public static Member load(MemberId id, String name, Position mainPosition, int generation, String statusMessage,
            AuthCredentials authCredentials, OAuthCredentials oAuthCredentials){
        return builder()
                .id(id)
                .name(name)
                .mainPosition(mainPosition)
                .generation(generation)
                .statusMessage(statusMessage)
                .authCredentials(authCredentials)
                .oAuthCredentials(oAuthCredentials)
                .build();
    }

    public void changePassword(String rawOldPassword, String rawNewPassword, PasswordEncoder passwordEncoder){
        if (authCredentials == null)
            throw new NotAuthMemberException();
        this.authCredentials = this.authCredentials.changePassword(rawOldPassword, rawNewPassword, passwordEncoder);
    }

    public void changeStatusMessage(String newStatusMessage){
        this.statusMessage = newStatusMessage;
    }
}
