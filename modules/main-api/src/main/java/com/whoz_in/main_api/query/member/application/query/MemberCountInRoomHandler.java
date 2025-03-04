package com.whoz_in.main_api.query.member.application.query;

import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.member.application.response.MemberCountInRoomResponse;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class MemberCountInRoomHandler implements QueryHandler<MemberCountInRoom, MemberCountInRoomResponse> {

    private final MemberViewer memberViewer;
    private final ActiveDeviceViewer activeDeviceViewer;

    @Override
    public MemberCountInRoomResponse handle(MemberCountInRoom query) {
        // 이렇게 간단한가..?
        return new MemberCountInRoomResponse(memberViewer.countActiveMember());
    }

}
