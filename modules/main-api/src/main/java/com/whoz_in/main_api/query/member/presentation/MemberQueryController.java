package com.whoz_in.main_api.query.member.presentation;

import com.whoz_in.main_api.query.member.application.detail.MemberDetail;
import com.whoz_in.main_api.query.member.application.detail.MemberDetailGet;
import com.whoz_in.main_api.query.member.application.in_room.MembersInRoomGet;
import com.whoz_in.main_api.query.member.application.in_room.MembersInRoom;
import com.whoz_in.main_api.query.member.application.profile.MemberProfileGet;
import com.whoz_in.main_api.query.member.application.profile.MemberProfile;
import com.whoz_in.main_api.query.member.presentation.docs.MemberQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MemberQueryController extends QueryController implements MemberQueryApi {

    public MemberQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @GetMapping("/members")
    public ResponseEntity<SuccessBody<MembersInRoom>> getActiveMembers(
            @RequestParam("size") int size,
            @RequestParam("page") int page,
            @RequestParam("sortType") String sortType,
            @RequestParam(value = "status", required = false) String status
    ) {
        MembersInRoomGet query = new MembersInRoomGet(page, size, sortType, status);
        MembersInRoom response = ask(query);
        return ResponseEntityGenerator.success(response, CrudResponseCode.READ);
    }

    @GetMapping("/member")
    public ResponseEntity<SuccessBody<MemberDetail>> getDetailInfo(){
        return ResponseEntityGenerator.success(ask(new MemberDetailGet()), CrudResponseCode.READ);
    }

    @GetMapping("/member/profile/{memberId}")
    public ResponseEntity<SuccessBody<MemberProfile>> getProfileInfo(@PathVariable("memberId") String memberId){
        return ResponseEntityGenerator.success(ask(new MemberProfileGet(UUID.fromString(memberId))), CrudResponseCode.READ);
    }

}
