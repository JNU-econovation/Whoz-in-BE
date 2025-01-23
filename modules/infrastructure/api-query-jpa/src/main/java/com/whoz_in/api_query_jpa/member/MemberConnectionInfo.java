package com.whoz_in.api_query_jpa.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Member 가 생성될 때 생성되어야 한다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberConnectionInfo {

    @Id
    private UUID memberId;

    private Duration dailyTime;

    private Duration totalTime;

    private boolean isActive;

    public MemberConnectionInfo(UUID memberId){
        this.memberId = memberId;
        this.dailyTime = Duration.ZERO;
        this.totalTime = Duration.ZERO;
        this.isActive = false;
    }

    public void resetDailyTime(){
        this.dailyTime = Duration.ZERO;
    }

    public void addDailyTime(Duration continuousTime){
        if(Objects.isNull(this.dailyTime)) this.dailyTime = Duration.from(continuousTime);
        this.dailyTime = this.dailyTime.plus(continuousTime);
    }

    public void addTotalTime(){
        if(Objects.isNull(this.totalTime)) this.totalTime = Duration.from(dailyTime);
        this.totalTime = totalTime.plus(this.dailyTime);
    }

    public void activeOn(){
        this.isActive = true;
    }

    public void inActiveOn(){
        this.isActive = false;
    }

    public static MemberConnectionInfo create(UUID memberId){
        return new MemberConnectionInfo(memberId);
    }

}
