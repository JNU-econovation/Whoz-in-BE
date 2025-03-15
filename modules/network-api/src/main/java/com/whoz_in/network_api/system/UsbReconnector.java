package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.process.TransientProcess;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsbReconnector {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleReconnection(String interfaceName) {
        if (scheduledTasks.containsKey(interfaceName)) {
            log.info("{}의 USB 초기화가 이미 예약되어 있습니다.", interfaceName);
            return;
        }

        ScheduledFuture<?> scheduledTask = scheduler.schedule(() -> {
            reconnect(interfaceName);
            scheduledTasks.remove(interfaceName);
        }, 10, TimeUnit.SECONDS);

        scheduledTasks.put(interfaceName, scheduledTask);
    }

    public void cancelReconnection(String interfaceName) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.remove(interfaceName);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    private void reconnect(String interfaceName) {
        log.info("{}의 USB 초기화 시작", interfaceName);

        // 네트워크 인터페이스의 USB 경로 가져오기
        String usbPath = getUsbPath(interfaceName);
        if (usbPath == null) {
            log.warn("{}는 USB 장치가 아니거나 USB 포트를 찾을 수 없음", interfaceName);
            return;
        }

        // USB 장치 존재 확인
        if (!Files.exists(Paths.get("/sys/bus/usb/devices/" + usbPath))) {
            log.warn("USB 장치가 존재하지 않음 (경로: /sys/bus/usb/devices/{})", usbPath);
            return;
        }

        try {
            // 네트워크 인터페이스 비활성화
            TransientProcess.create("sudo ip link set " + interfaceName + " down").waitTermination();
            // USB 장치 언바인드
            writeToFile("/sys/bus/usb/drivers/usb/unbind", usbPath);
            sleep(1000);
            // USB 장치 바인드
            writeToFile("/sys/bus/usb/drivers/usb/bind", usbPath);
            // 네트워크 인터페이스 활성화
            TransientProcess.create("sudo ip link set " + interfaceName + " up").waitTermination();
            log.info("{}의 USB 초기화 완료", interfaceName);

        } catch (Exception e) {
            log.error("{} USB 초기화 중 오류 발생: {}", interfaceName, e.getMessage());
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            log.error("파일 쓰기 실패: {} → {}", filePath, e.getMessage());
        }
    }

    // 네트워크 인터페이스의 USB 경로를 찾음
    private String getUsbPath(String interfaceName) {
        try {
            File interfacePath = new File("/sys/class/net/" + interfaceName);
            if (!interfacePath.exists()) {
                return null;
            }

            // 심볼릭 링크가 가르키는 실제 경로로 변환
            // 예시: /sys/class/net/wlan0 -> ../../devices/pci0000:00/0000:00:14.0/usb1/1-8/1-8:1.0/net/wlan0
            File realPath = interfacePath.getCanonicalFile();
            // 상대 경로인 실제 경로를 절대 경로로 변환
            // 위를 기준으로 /sys/devices/pci0000:00/......으로 바뀌게 됨
            String absolutePath = realPath.getAbsolutePath();

            String[] pathSegments = absolutePath.split("/");
            for (String pathSegment : pathSegments) {
                if (pathSegment.matches("\\d+-\\d+(\\.\\d+)?")) {
                    return pathSegment;
                }
            }
        } catch (Exception e) {
            log.error("USB 경로를 찾는 중 오류 발생", e);
        }
        return null;
    }
}