package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.main_api.query.member.application.MemberName;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class ActiveDeviceListHandler implements QueryHandler<ActiveDeviceList, ActiveDeviceListResponse> {

    private final ActiveDeviceViewer activeDeviceViewer;
    private final MemberViewer memberViewer;

    @Override
    public ActiveDeviceListResponse handle(ActiveDeviceList query) {
        int page = query.page();
        int size = query.size();
        String sortType = query.sortType();

        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();
        List<ActiveDeviceResponse> responses = new ArrayList<>();

        int start = page * size;
        int end = start + size;

        for(int i = start; i<end; i++){
            ActiveDevice activeDevice = activeDevices.get(i);
            String memberName = getMemberName(activeDevice.memberId().toString());
            ActiveDeviceResponse oneResponse = new ActiveDeviceResponse(
                    activeDevice.deviceId().toString(),
                    activeDevice.memberId().toString(),
                    memberName,
                    Duration.between(activeDevice.disconnectedTime(), activeDevice.connectedTime()).toString(),
                    activeDevice.totalConnectedTime().toString()
            );
            responses.add(oneResponse);
        }

        // TODO : 정렬 자동화
        if(sortType.equals("asc"))
            responses.sort(Comparator.comparing(ActiveDeviceResponse::memberName));
        else
            responses.sort(Comparator.comparing(ActiveDeviceResponse::totalActiveTime));

        return new ActiveDeviceListResponse(responses);
    }

    private String getMemberName(String memberId){
        return memberViewer.findNameByMemberId(memberId)
                .map(MemberName::memberName)
                .orElseThrow(() -> new IllegalArgumentException("사용자 기기와 사용자 이름 매핑 중 예상치 못한 에러 발생"));
    }
}
