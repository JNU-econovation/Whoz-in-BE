package com.whoz_in.domain.member.model;


import com.whoz_in.domain.badge.exception.BadgeCurrentHidedException;
import com.whoz_in.domain.badge.exception.NoBadgeException;
import com.whoz_in.domain.badge.model.BadgeId;
import com.whoz_in.domain.shared.AggregateRoot;
import com.whoz_in.shared.domain_event.member.MemberBadgeVisibilityChanged;
import com.whoz_in.shared.domain_event.member.MemberCreated;
import com.whoz_in.shared.domain_event.member.MemberStatusMessageChanged;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    private OAuthCredentials oAuthCredentials;
    private final Map<BadgeId, Boolean> badges; // Map<가진뱃지, 보여줌?>
    @Getter private BadgeId mainBadge; // 대표 뱃지

    public OAuthCredentials getOAuthCredentials(){
        return oAuthCredentials;
    }

    public static Member create(String name, Position mainPosition, int generation,
            OAuthCredentials oAuthCredentials, BadgeId badgeId){
        Map<BadgeId, Boolean> badges = new HashMap<>();
        badges.put(badgeId, true);
        Member member = builder()
                .id(new MemberId())
                .name(name)
                .mainPosition(mainPosition)
                .generation(generation)
                .statusMessage("")
                .oAuthCredentials(oAuthCredentials)
                .badges(badges)
                .mainBadge(badgeId)
                .build();
        member.register(new MemberCreated(
                member.getId().id(),
                name,
                mainPosition.getName(),
                generation,
                "",
                oAuthCredentials.getSocialProvider().name(),
                oAuthCredentials.getSocialId(),
                badges.entrySet().stream()
                        .collect(Collectors.toMap(
                                (Map.Entry<BadgeId, Boolean> e) -> e.getKey().id(),
                                Map.Entry::getValue
                        )),
                badgeId.id()
        ));
        return member;
    }

    public static Member load(MemberId id, String name, Position mainPosition, int generation, String statusMessage,
                              OAuthCredentials oAuthCredentials, Map<BadgeId, Boolean> badges, BadgeId mainBadge){
        return builder()
                .id(id)
                .name(name)
                .mainPosition(mainPosition)
                .generation(generation)
                .statusMessage(statusMessage)
                .oAuthCredentials(oAuthCredentials)
                .badges(badges)
                .mainBadge(mainBadge)
                .build();
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
