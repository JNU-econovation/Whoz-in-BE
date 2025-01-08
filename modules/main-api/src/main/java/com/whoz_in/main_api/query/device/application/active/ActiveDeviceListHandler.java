package com.whoz_in.main_api.query.device.application.active;

import com.whoz_in.main_api.query.member.application.MemberName;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.query.shared.application.QueryHandler;
import com.whoz_in.main_api.shared.application.Handler;
import java.time.Duration;
import java.time.LocalDateTime;
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
        int page = query.page() - 1;
        int size = query.size();
        String sortType = query.sortType();

        List<ActiveDevice> activeDevices = activeDeviceViewer.findAll();
        List<ActiveDeviceResponse> responses = new ArrayList<>();

        if(!activeDevices.isEmpty()) {

            int start = page * size;
            int end = Math.min((start + size), activeDevices.size());

            for (int i = start; i < end; i++) {
                ActiveDevice activeDevice = activeDevices.get(i);

                String deviceId = activeDevice.deviceId().toString();
                String memberId = activeDevice.memberId().toString();
                String memberName = getMemberName(activeDevice.memberId().toString());
                Duration continuousTime = activeDevice.continuousTime();
                Duration totalConnectedTime = activeDevice.totalConnectedTime();

                ActiveDeviceResponse oneResponse = new ActiveDeviceResponse(
                        deviceId,
                        memberId,
                        memberName,
                        String.format("%s시간 %s분", continuousTime.toHours(), continuousTime.toMinutes()),
                        String.format("%s시간 %s분", totalConnectedTime.toHours(), totalConnectedTime.toMinutes())
                );
                responses.add(oneResponse);
            }

            // TODO : 정렬 자동화
            if (sortType.equals("asc"))
                responses.sort(Comparator.comparing(ActiveDeviceResponse::memberName));
            else
                responses.sort(Comparator.comparing(ActiveDeviceResponse::totalActiveTime));

            return new ActiveDeviceListResponse(responses);
        }

        return new ActiveDeviceListResponse(responses);
    }

    private String calculateContinuousTime(ActiveDevice activeDevice){
        return "";
    }

    private String getMemberName(String memberId){
        return memberViewer.findNameByMemberId(memberId)
                .map(MemberName::memberName)
                .orElseThrow(() -> new IllegalArgumentException("사용자 기기와 사용자 이름 매핑 중 예상치 못한 에러 발생"));
    }
}
