package com.whoz_in.api_query_jpa.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Duration;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Entity
@Subselect("SELECT "
        + "m.id,"
        + "m.login_id,"
        + "m.password,"
        + "m.generation,"
        + "m.name, "
        + "m.position, "
        + "m.status_message, "
        + "b.id AS main_badge_id, "
        + "b.name AS main_badge_name, "
        + "b.color_code AS main_badge_color, "
        + "a.active_time AS total_active_time "
        + "FROM member_entity m "
        + "LEFT JOIN badge_entity b ON m.main_badge = b.id "
        + "LEFT JOIN activity_history a ON m.id = a.member_id AND a.time_unit = 'TOTAL'"
)
@Immutable
@Synchronize({"member_entity", "badge_entity", "activity_history"})

public class Member {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "generation")
    private int generation;

    @Column(name =  "position")
    private String position;

    @Column(name = "status_message")
    private String statusMessage;

    @Column(name = "main_badge_id")
    private UUID mainBadgeId;

    @Column(name = "main_badge_name")
    private String mainBadgeName;

    @Column(name = "main_badge_color")
    private String mainBadgeColor;

    @Column(name = "total_active_time")
    private Duration totalActiveTime;

    /**
     * {@link com.whoz_in.api_query_jpa.member.activity.daily.DailyActivityAggregator}가
     * {@link com.whoz_in.api_query_jpa.member.activity.history.ActivityHistory}에 캐싱한
     * total active time임
     * 따라서 오늘의 데이터는 아직 반영이 안된 상태니까 사용하는 쪽에서 오늘의 active time을 더해줘야 함.
     */
    public Duration getTotalActiveTime() {
        return totalActiveTime != null ? totalActiveTime : Duration.ZERO;
    }
}
