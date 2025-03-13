//package com.whoz_in.network_api.system;
//
//import com.whoz_in.network_api.common.network_interface.NetworkInterfaceStatusEvent;
//import com.whoz_in.network_api.common.process.TransientProcess;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class UsbRebinder {
//
//    @EventListener
//    private void handle(NetworkInterfaceStatusEvent event){
//        // DIS
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.schedule(() -> {
//            rebind(event.interfaceName());
//        }, 10000, TimeUnit.MILLISECONDS);
//    }
//
//    private void rebind(String interfaceName) {
//        log.info("{}의 USB 초기화 시작", interfaceName);
//
//        // 네트워크 인터페이스의 USB 경로 가져오기
//        String usbPath = getUsbPath(interfaceName);
//        // usb인 장치가 아니거나 usb 포트가 제대로 동작 안할경우
//        if (usbPath == null) {
//            log.warn("{}의 USB 장치를 찾을 수 없습니다.", interfaceName);
//            return;
//        }
//
//        // USB 장치 존재 확인
//        if (!Files.exists(Paths.get("/sys/bus/usb/devices/" + usbPath))) {
//            log.warn("USB 장치가 존재하지 않음 (경로: /sys/bus/usb/devices/{})", usbPath);
//            return;
//        }
//
//        // 네트워크 인터페이스 비활성화
//        TransientProcess.create("sudo ip link set " + interfaceName + " down").waitTermination();
//        // USB 장치 언바인드
//        writeToFile("/sys/bus/usb/drivers/usb/unbind", usbPath);
//        sleep(1000);
//        // USB 장치 바인드
//        writeToFile("/sys/bus/usb/drivers/usb/bind", usbPath);
//        // 네트워크 인터페이스 활성화
//        TransientProcess.create("sudo ip link set " + interfaceName + " up").waitTermination();
//
//        log.info("USB 초기화 완료 - ({})", interfaceName);
//    }
//
//    private void sleep(long millis){
//        try {
//            Thread.sleep(millis);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//    private void writeToFile(String filePath, String content) {
//        try (FileWriter writer = new FileWriter(filePath)) {
//            writer.write(content);
//            writer.flush();
//        } catch (IOException e) {
//            log.error("파일 쓰기 실패: {} → {}", filePath, e.getMessage());
//        }
//    }
//
//    // 네트워크 인터페이스의 USB 경로를 찾음
//    private String getUsbPath(String interfaceName) {
//        try {
//            File interfacePath = new File("/sys/class/net/" + interfaceName);
//            if (!interfacePath.exists()) {
//                return null;
//            }
//
//            File realPath = interfacePath.getCanonicalFile();
//            String absolutePath = realPath.getAbsolutePath();
//
//            String[] pathSegments = absolutePath.split("/");
//            for (String pathSegment : pathSegments) {
//                if (pathSegment.startsWith("usb")) {
//                    return pathSegment;
//                }
//            }
//        } catch (Exception e) {
//            log.error("USB 경로를 찾는 중 오류 발생", e);
//        }
//        return null;
//    }
//}