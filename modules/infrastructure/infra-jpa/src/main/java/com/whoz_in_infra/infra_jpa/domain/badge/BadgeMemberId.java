package com.whoz_in_infra.infra_jpa.domain.badge;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BadgeMemberId implements Serializable {
    private UUID memberId;
    private UUID badgeId;
}
