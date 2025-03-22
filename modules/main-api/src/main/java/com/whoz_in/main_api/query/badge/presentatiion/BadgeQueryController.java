package com.whoz_in.main_api.query.badge.presentatiion;

import com.whoz_in.main_api.query.badge.application.query.BadgeId;
import com.whoz_in.main_api.query.badge.application.query.EmptyMemberBadgeQuery;
import com.whoz_in.main_api.query.badge.application.query.EmptyRegisterableBadgeQuery;
import com.whoz_in.main_api.query.badge.application.response.BadgeInfoResponse;
import com.whoz_in.main_api.query.badge.application.view.BadgesOfMember;
import com.whoz_in.main_api.query.badge.application.view.RegisterableBadges;
import com.whoz_in.main_api.query.badge.presentatiion.docs.BadgeQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BadgeQueryController extends QueryController implements BadgeQueryApi {
    public BadgeQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @Override
    @GetMapping("/badges")
    public ResponseEntity<SuccessBody<BadgeInfoResponse>> viewBadgeInfo(@RequestBody BadgeId request) {
        BadgeInfoResponse r = ask(request);
        return ResponseEntityGenerator.success(r, CrudResponseCode.READ);
    }

    @Override
    @GetMapping("/badges/register")
    public ResponseEntity<SuccessBody<RegisterableBadges>> viewRegisterable() {
        RegisterableBadges r = ask(new EmptyRegisterableBadgeQuery());
        return ResponseEntityGenerator.success(r,CrudResponseCode.READ);
    }

    @Override
    @GetMapping("/badges/members")
    public ResponseEntity<SuccessBody<BadgesOfMember>> viewBadgesOfMember() {
         BadgesOfMember r = ask(new EmptyMemberBadgeQuery());
        return ResponseEntityGenerator.success(r,CrudResponseCode.READ);
    }
}
