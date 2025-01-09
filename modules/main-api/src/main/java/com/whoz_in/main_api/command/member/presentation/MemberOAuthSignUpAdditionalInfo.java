package com.whoz_in.main_api.command.member.presentation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.whoz_in.domain.member.model.Position;

//소셜 로그인 후에 추가로 받아야 할 정보
public record MemberOAuthSignUpAdditionalInfo(
        String name,
        @JsonDeserialize(using = PositionDeserializer.class)
        Position position,
        Integer generation
) {}