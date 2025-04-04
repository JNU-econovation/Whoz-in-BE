package com.whoz_in.domain.member.model;


import com.whoz_in.domain.badge.exception.BadgeCurrentHidedException;
import com.whoz_in.domain.badge.exception.NoBadgeException;
import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.member.exception.NotAuthMemberException;
import com.whoz_in.domain.member.service.PasswordEncoder;
import com.whoz_in.domain.shared.AggregateRoot;
import com.whoz_in.shared.Nullable;
import com.whoz_in.shared.domain_event.member.MemberBadgeVisibilityChanged;
import com.whoz_in.shared.domain_event.member.MemberCreated;
import com.whoz_in.shared.domain_event.member.MemberPasswordChanged;
import com.whoz_in.shared.domain_event.member.MemberStatusMessageChanged;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final Map<BadgeId, Boolean> badges; // Map<가진뱃지, 보여줌?>
    @Getter private BadgeId mainBadge; // 대표 뱃지

    //일반 로그인이 아닐수도 있으므로 Optional
    public Optional<AuthCredentials> getAuthCredentials(){
        return Optional.ofNullable(authCredentials);
    }
    //소셜 로그인이 아닐수도 있으므로 Optional
    public Optional<OAuthCredentials> getOAuthCredentials(){
        return Optional.ofNullable(oAuthCredentials);
    }

    //일반 회원가입
    public static Member create(String name, Position mainPosition, int generation, AuthCredentials authCredentials){
        return create(name, mainPosition, generation, authCredentials, null, new HashMap<>(), null);
    }
    //소셜 회원가입
    public static Member create(String name, Position mainPosition, int generation, OAuthCredentials oAuthCredentials, BadgeId mainBadge){
        return create(name, mainPosition, generation, null, oAuthCredentials, new HashMap<>(), mainBadge);
    }

    private static Member create(String name, Position mainPosition, int generation,
                                 AuthCredentials authCredentials, OAuthCredentials oAuthCredentials, Map<BadgeId, Boolean> badges, BadgeId mainBadge){
        if (authCredentials == null && oAuthCredentials == null)
            throw new IllegalStateException("no auth and oauth");
        Member member = builder()
                .id(new MemberId())
                .name(name)
                .mainPosition(mainPosition)
                .generation(generation)
                .statusMessage("")
                .authCredentials(authCredentials)
                .oAuthCredentials(oAuthCredentials)
                .badges(badges)
                .mainBadge(mainBadge)
                .build();
        member.register(new MemberCreated(
                member.getId().id(),
                name,
                mainPosition.getName(),
                generation,
                "",
                authCredentials != null ? authCredentials.getLoginId() : null,
                oAuthCredentials != null ? oAuthCredentials.getSocialProvider().name() : null,
                oAuthCredentials != null ? oAuthCredentials.getSocialId() : null,
                badges.entrySet().stream()
                        .collect(Collectors.toMap(
                                (Map.Entry<BadgeId, Boolean> e) -> e.getKey().id(),
                                Map.Entry::getValue
                        )),
                mainBadge != null ? mainBadge.id() : null     // 대표 뱃지
        ));
        return member;
    }

    public static Member load(MemberId id, String name, Position mainPosition, int generation, String statusMessage,
                              AuthCredentials authCredentials, OAuthCredentials oAuthCredentials, Map<BadgeId, Boolean> badges, BadgeId mainBadge){
        return builder()
                .id(id)
                .name(name)
                .mainPosition(mainPosition)
                .generation(generation)
                .statusMessage(statusMessage)
                .authCredentials(authCredentials)
                .oAuthCredentials(oAuthCredentials)
                .badges(badges)
                .mainBadge(mainBadge)
                .build();
    }

    public void changePassword(String rawOldPassword, String rawNewPassword, PasswordEncoder passwordEncoder){
        if (authCredentials == null)
            throw NotAuthMemberException.EXCEPTION;
        this.authCredentials = this.authCredentials.changePassword(rawOldPassword, rawNewPassword, passwordEncoder);
        this.register(new MemberPasswordChanged(this.getId().id()));
    }

    public void changeStatusMessage(String newStatusMessage){
        this.statusMessage = newStatusMessage;
        this.register(new MemberStatusMessageChanged(this.getId().id(), this.statusMessage));
    }

    public void changeBadgeVisibility(BadgeId badgeId, boolean show) {
        this.badges.put(badgeId, show);
        this.register(new MemberBadgeVisibilityChanged(this.getId().id(), badgeId.id(), show));
    }

    public void attachBadge(BadgeId badgeId) {
        this.badges.put(badgeId, true);
    }

    public Map<BadgeId, Boolean> getBadges() {
        return Collections.unmodifiableMap(this.badges);
    }

    public void changeMainBadge(BadgeId badgeId) {
        if (!badges.containsKey(badgeId)) {
            throw NoBadgeException.EXCEPTION;
        }
        if (!badges.get(badgeId)) {
            throw BadgeCurrentHidedException.EXCEPTION;
        }
        this.mainBadge = badgeId;
    }
}
