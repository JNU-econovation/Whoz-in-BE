package com.whoz_in.network_api.monitor;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.process.TransientProcess;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//모니터 모드인 네트워크 인터페이스의 채널을 주기적으로 바꿔주는 역할
//변경될 채널은 주변 모든 와이파이들을 대상으로 함.
@Slf4j
@Profile("prod")
@Component
public final class ChannelHopper {
    private final NetworkInterface monitorNI;
    private final Set<Integer> channelsToHop = new HashSet<>();

    public ChannelHopper(NetworkInterfaceProfileConfig config) {
        this.monitorNI = config.getMonitorProfile().ni();
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
    public void hop(){
        //hopping할 채널이 없을경우 채널 불러오기
        if (channelsToHop.isEmpty()) {
            Set<Integer> channels = loadChannelsToHop();
            log.info("channels to hop : " + channels);
            if (channels.isEmpty()) return; // 주변에 채널이 없을경우 종료 (물론 이럴 일은 없음)
            channelsToHop.addAll(channels);
        }

        //hop channel
        Integer channel = channelsToHop.iterator().next();
        String hopCommand = "sudo -S iwconfig %s channel %d".formatted(monitorNI.getName(), channel);
        TransientProcess.create(hopCommand);
        //hopping된 채널 삭제
        channelsToHop.remove(channel);
    }

    //주변 채널을 가져옵니다
    private Set<Integer> loadChannelsToHop(){
        return TransientProcess.create("nmcli -f SSID,CHAN dev wifi").results()
                .stream()
                .map(line -> line.trim().split("\\s+"))
                .filter(split -> (split.length == 2) && split[1].matches("\\d+"))
                .map(split -> Integer.parseInt(split[1]))
                .collect(Collectors.toSet());
    }
}
