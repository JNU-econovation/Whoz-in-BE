package com.whoz_in.api_query_jpa.monitor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Subselect;

@Entity
@Subselect("select * from monitor_log_entity")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MonitorLog {

    @Id
    private String mac;

    private LocalDateTime cratedAt;

    private LocalDateTime updatedAt;

}
