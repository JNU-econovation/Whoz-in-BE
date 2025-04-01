package com.whoz_in.main_api.shared.domain.badge;

import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;

public class BadgeFixture {

    public static BadgeInfo badgeInfo(){
        return new BadgeInfo(
                "badgeName",
                "badgeColorCode",
                "badgeDescription"
        );
    }

}
