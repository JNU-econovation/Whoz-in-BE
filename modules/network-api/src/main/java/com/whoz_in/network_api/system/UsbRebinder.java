package com.whoz_in.network_api.system;

import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent.Status;
import com.whoz_in.network_api.common.process.TransientProcess;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsbRebinder {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    // TODO: 이 메서드 예약 메서드와 리스너 메서드로 분리하고 리스너는 다른 클래스로 분리. rebind는 그대로 private
    @EventListener
    private void handle(NetworkInterfaceStatusEvent event) {
        String interfaceName = event.interfaceName();
        Status status = event.status();

        if (status == Status.DISCONNECTED || status == Status.REMOVED) {
            // 이미 예약된 작업이 있으면 중복 예약 방지
            if (scheduledTasks.containsKey(interfaceName)) {
                log.info("{}의 USB 초기화가 이미 예약됨", interfaceName);
                return;
            }

            // 10초 후에 rebind 실행 예약 (자동으로 다시 연결되는 경우와 일시적인 오류로 끊기는 경우를 대비하기 위함)
            ScheduledFuture<?> scheduledTask = scheduler.schedule(() -> {
                rebind(interfaceName);
                scheduledTasks.remove(interfaceName); // 실행 후 예약 제거
            }, 10, TimeUnit.SECONDS);

            scheduledTasks.put(interfaceName, scheduledTask);
            log.info("{}의 USB 초기화가 예약됐습니다. (10초 후 실행)", interfaceName);

        } else if (status == Status.RECONNECTED || status == Status.ADDED) {
            // 예약된 초기화 작업이 있으면 취소
            ScheduledFuture<?> scheduledTask = scheduledTasks.remove(interfaceName);
            if (scheduledTask != null) {
                scheduledTask.cancel(false);
                log.info("{}의 USB 초기화 예약이 취소됐습니다. (다시 연결됐거나 나타남)", interfaceName);
            }
        }
    }

    private void rebind(String interfaceName) {
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