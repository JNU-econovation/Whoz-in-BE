package com.whoz_in.log_writer.monitor;

import com.whoz_in.log_writer.common.NetworkInterface;
import com.whoz_in.log_writer.common.process.TransientProcess;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

//모니터 모드인 네트워크 인터페이스의 채널을 주기적으로 바꿔주는 역할
//변경될 채널은 주변 모든 와이파이들을 대상으로 함.
@Slf4j
public class ChannelHopper {
    private final NetworkInterface monitor;
    private final String sudoPassword;
    private final Set<Integer> channelsToHop = new HashSet<>();

    public ChannelHopper(NetworkInterface monitor, @Value("${sudo_password}") String sudoPassword) {
        this.sudoPassword = sudoPassword;
        this.monitor = monitor;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
    public void hop(){
        if (!channelsToHop.iterator().hasNext()) {
            Set<Integer> channels = new TransientProcess("nmcli -f SSID,CHAN dev wifi").results()
                    .stream()
                    .map(line -> line.trim().split("\\s+"))
                    .filter(split -> (split.length == 2) && split[1].matches("\\d+"))
                    .map(split -> Integer.parseInt(split[1]))
                    .collect(Collectors.toSet());
            log.info("channels to hop : "+channels);
            channelsToHop.addAll(channels);
        }
        Integer channel = channelsToHop.iterator().next();
        channelsToHop.remove(channel);
        String hopCommand = "sudo -S iwconfig %s channel %d".formatted(monitor.getName(), channel);
        new TransientProcess(hopCommand, sudoPassword);
    }
}
