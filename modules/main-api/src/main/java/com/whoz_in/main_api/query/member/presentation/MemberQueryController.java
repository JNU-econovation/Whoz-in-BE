package com.whoz_in.main_api.query.member.presentation;

import com.whoz_in.main_api.query.member.application.query.MemberCountInRoom;
import com.whoz_in.main_api.query.member.application.query.MemberDetailInfoGet;
import com.whoz_in.main_api.query.member.application.query.MemberProfile;
import com.whoz_in.main_api.query.member.application.query.MembersInRoom;
import com.whoz_in.main_api.query.member.application.response.MemberCountInRoomResponse;
import com.whoz_in.main_api.query.member.application.response.MemberProfileInfo;
import com.whoz_in.main_api.query.member.application.response.MembersInRoomResponse;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.query.member.presentation.docs.MemberQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
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
    public ResponseEntity<SuccessBody<MembersInRoomResponse>> getActiveMembers(
            @RequestParam("size") int size,
            @RequestParam("page") int page,
            @RequestParam("sortType") String sortType,
            @RequestParam(value = "status", required = false) String status
    ) {
        MembersInRoom query = new MembersInRoom(page, size, sortType, status);
        MembersInRoomResponse response = ask(query);
        return ResponseEntityGenerator.success(response, CrudResponseCode.READ);
    }

    @GetMapping("/members/active/size")
    public ResponseEntity<SuccessBody<MemberCountInRoomResponse>> getActiveMemberCount(){
        return ResponseEntityGenerator.success(ask(new MemberCountInRoom()), CrudResponseCode.READ);
    }

    @GetMapping("/member")
    public ResponseEntity<SuccessBody<MemberDetailInfo>> getDetailInfo(){
        return ResponseEntityGenerator.success(ask(new MemberDetailInfoGet()), CrudResponseCode.READ);
    }

    @GetMapping("/member/{memberId}/profile")
    public ResponseEntity<SuccessBody<MemberProfileInfo>> getProfileInfo(@PathVariable("memberId") String memberId){
        return ResponseEntityGenerator.success(ask(new MemberProfile(memberId)), CrudResponseCode.READ);
    }

}
