package com.whoz_in.api_query_jpa.member.activity.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 멤버들의 재실 기록 projection입니다. <br>
 원본 데이터인 {@link com.whoz_in.api_query_jpa.device.connection.DeviceConnection}을 가지고도 재실 기록을 계산할 수 있지만 비효율적이기 때문에 이 테이블에 미리 집계해두고 조회합니다.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "activity_history",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "reference_date", "time_unit"})
        }
)
public class ActivityHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID memberId;

    @Column
    private LocalDate referenceDate;  // 해당 단위의 시작일

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit; // TOTAL은 date 상관없음

    @Column(nullable = false)
    private Duration activeTime;

    public void add(Duration more) {
        if (more == null || more.isNegative()) {
            throw new IllegalArgumentException("추가할 시간이 잘못되었습니다");
        }
        this.activeTime = this.activeTime.plus(more);
    }
}
