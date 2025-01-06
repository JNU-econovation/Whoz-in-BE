package com.whoz_in.main_api.command.member.application;

import com.whoz_in.main_api.command.shared.application.Command;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfo;

public record MemberOAuth2Login(
        OAuth2UserInfo oAuth2UserInfo
) implements Command {}
