package com.whoz_in.network_api.system;

import static com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus.*;

import com.whoz_in.network_api.common.network_interface.NetworkInterface;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatus;
import com.whoz_in.network_api.common.network_interface.WirelessMode;
import com.whoz_in.network_api.common.LinuxCondition;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceManager;
import com.whoz_in.network_api.config.NetworkInterfaceProfileConfig;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(LinuxCondition.class)
@RequiredArgsConstructor
public class NetworkInterfaceRestorer {
    private static final Duration INITIAL_DELAY = Duration.ofSeconds(10);
    private static final Duration RETRY_DELAY = Duration.ofMinutes(1);

    @Qualifier("threadPoolTaskScheduler")
    private final TaskScheduler scheduler;
    private final NetworkInterfaceManager networkInterfaceManager;
    private final NetworkInterfaceProfileConfig profileConfig;
    private final UsbReconnector reconnector;
    private final Map<String, ScheduledFuture<?>> recoveryTasks = new ConcurrentHashMap<>();

    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        String interfaceName = event.interfaceName();
        NetworkInterfaceStatus status = event.status();
        if (!isManagedInterface(interfaceName)) return;

        if (status == DISCONNECTED || status == REMOVED) {
            scheduleRecovery(interfaceName, INITIAL_DELAY);
        } else if (status == RECONNECTED || status == ADDED_AND_RECONNECTED) {
            clearRecovery(interfaceName);
        }
    }

    private boolean isManagedInterface(String interfaceName) {
        Set<String> managedInterfaceNames = profileConfig.getManagedProfiles().stream()
                .map(profile -> profile.interfaceName())
                .collect(java.util.stream.Collectors.toSet());
        return managedInterfaceNames.contains(interfaceName);
    }

    private void scheduleRecovery(String interfaceName, Duration delay) {
        recoveryTasks.computeIfAbsent(interfaceName, ignored -> {
            log.info("{}의 복구 검증이 예약됐습니다. ({}초 후 실행)", interfaceName, delay.toSeconds());
            return scheduler.schedule(
                    () -> verifyAndRecover(interfaceName),
                    Instant.now().plus(delay)
            );
        });
    }

    private void verifyAndRecover(String interfaceName) {
        recoveryTasks.remove(interfaceName);

        if (isRecovered(interfaceName)) {
            log.info("{}는 이미 복구되어 추가 USB 초기화를 수행하지 않습니다.", interfaceName);
            return;
        }

        reconnector.reconnect(interfaceName);
        scheduleRecovery(interfaceName, RETRY_DELAY);
    }

    private boolean isRecovered(String interfaceName) {
        NetworkInterface networkInterface = networkInterfaceManager.get().get(interfaceName);
        if (networkInterface == null) return false;
        return networkInterface.isConnected()
                && networkInterface.isWireless()
                && networkInterface.getWirelessInfo().mode() == WirelessMode.MANAGED;
    }

    private void clearRecovery(String interfaceName) {
        ScheduledFuture<?> scheduledFuture = recoveryTasks.remove(interfaceName);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            log.info("{}의 복구 검증 예약이 취소됐습니다. (다시 연결됨)", interfaceName);
        }
    }
}
