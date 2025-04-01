package com.whoz_in.main_api.query.badge.presentatiion;

import com.whoz_in.main_api.query.badge.application.query.BadgeInfoGet;
import com.whoz_in.main_api.query.badge.application.query.MemberBadgeQuery;
import com.whoz_in.main_api.query.badge.application.query.RegistrableBadgeQuery;
import com.whoz_in.main_api.query.badge.application.view.BadgeInfo;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.badge.application.view.RegistrableBadges;
import com.whoz_in.main_api.query.badge.presentatiion.docs.BadgeQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BadgeQueryController extends QueryController implements BadgeQueryApi {
    public BadgeQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @Override
    @GetMapping("/badges")
    public ResponseEntity<SuccessBody<BadgeInfo>> viewBadgeInfo(@RequestParam("badgeId") UUID badgeId) {
        BadgeInfoGet request = new BadgeInfoGet(badgeId);
        BadgeInfo r = ask(request);
        return ResponseEntityGenerator.success(r, CrudResponseCode.READ);
    }

    @Override
    @GetMapping("/badges/register")
    public ResponseEntity<SuccessBody<RegistrableBadges>> viewRegisterable() {
        RegistrableBadges r = ask(new RegistrableBadgeQuery());
        return ResponseEntityGenerator.success(r,CrudResponseCode.READ);
    }

    @Override
    @GetMapping("/badges/members")
    public ResponseEntity<SuccessBody<BadgesOfMember>> viewBadgesOfMember() {
         BadgesOfMember r = ask(new MemberBadgeQuery());
        return ResponseEntityGenerator.success(r,CrudResponseCode.READ);
    }
}
