package com.whoz_in.main_api.query.member.presentation;

import com.whoz_in.main_api.query.member.application.query.MemberCountInRoom;
import com.whoz_in.main_api.query.member.application.query.MembersInRoom;
import com.whoz_in.main_api.query.member.application.response.MemberCountInRoomResponse;
import com.whoz_in.main_api.query.member.application.response.MembersInRoomResponse;
import com.whoz_in.main_api.query.member.application.view.MemberDetailInfo;
import com.whoz_in.main_api.query.member.application.query.MemberDetailInfoGet;
import com.whoz_in.main_api.query.member.presentation.docs.MemberQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MemberQueryController extends QueryController implements MemberQueryApi {

    public MemberQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    //TODO: Response 클래스 이름 Member 기준으로 변경
    @GetMapping("/members")
    public ResponseEntity<SuccessBody<MembersInRoomResponse>> getActiveDevices(
            @RequestParam("size") int size,
            @RequestParam("page") int page,
            @RequestParam("sortType") String sortType,
            @RequestParam("status") String status
    ) {
        MembersInRoom query = new MembersInRoom(page, size, sortType, status);
        MembersInRoomResponse response = ask(query);
        return ResponseEntityGenerator.success(response, CrudResponseCode.READ);
    }

    @GetMapping("/members/active/size")
    public ResponseEntity<SuccessBody<MemberCountInRoomResponse>> getActiveDevices(){
        return ResponseEntityGenerator.success(ask(new MemberCountInRoom()), CrudResponseCode.READ);
    }

    @GetMapping("/member")
    public ResponseEntity<SuccessBody<MemberDetailInfo>> getDetailInfo(){
        return ResponseEntityGenerator.success(ask(new MemberDetailInfoGet()), CrudResponseCode.READ);
    }

}
