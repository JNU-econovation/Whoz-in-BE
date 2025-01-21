package com.whoz_in.api_query_jpa.device.schedule;

import com.whoz_in.api_query_jpa.device.active.ActiveDeviceRepository;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfo;
import com.whoz_in.api_query_jpa.member.MemberConnectionInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DayEndScheduler {

    private final ActiveDeviceRepository activeDeviceRepository;
    private final MemberConnectionInfoRepository connectionInfoRepository;

    // 하루가 지나가면, 하루 누적 접속 시간을 전체 누적 접속 시간에 더한다.
    // 그 후 하루 누적 접속 시간을 초기화 한다.
    @Scheduled(cron ="0 0 0 * * *")
    public void resetDailyActiveTime(){
        List<MemberConnectionInfo> connectionInfos = connectionInfoRepository.findAll();

        List<MemberConnectionInfo> actives = filterActive(connectionInfos);
        List<MemberConnectionInfo> inActives = filterInActive(connectionInfos);

        save(actives);
        save(inActives);
    }

    private List<MemberConnectionInfo> filterActive(List<MemberConnectionInfo> connectionInfos){
        return connectionInfos.stream()
                .filter(MemberConnectionInfo::isActive)
                .toList();
    }

    private List<MemberConnectionInfo> filterInActive(List<MemberConnectionInfo> connectionInfos){
        return connectionInfos.stream()
                .filter(connectionInfo -> !connectionInfo.isActive())
                .toList();
    }

    private void save(List<MemberConnectionInfo> connectionInfos){
        connectionInfos.forEach(this::dayEnd);
        connectionInfoRepository.saveAll(connectionInfos);
    }

    private void dayEnd(MemberConnectionInfo connectionInfo){
        if(connectionInfo.isActive()){
            connectionInfo.addDailyTime();
            connectionInfo.resetContinuousTime();
        }

        connectionInfo.addTotalTime();
        connectionInfo.resetDailyTime();
    }

}
