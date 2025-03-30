package com.whoz_in.main_api.shared.domain.badge;

import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.view.BadgeName;

public class BadgeFixture {

    public static BadgeInfo badgeInfo(){
        return new BadgeInfo(
                "badgeName",
                "badgeColorCode",
                "badgeDescription"
        );
    }

    public static BadgeName badgeName(){
        return new BadgeName(
                "badgeName",
                "badgeColorCode"
        );
    }

}
