package com.whoz_in.log_writer;


import com.whoz_in.log_writer.common.NetworkInterface;
import com.whoz_in.log_writer.common.process.TransientProcess;
import com.whoz_in.log_writer.config.NetworkConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

//서버 시작 시 시스템을 검증함
//또한 주기적으로 시스템을 검증함
@Slf4j
public final class SystemValidator {
    private final NetworkConfig config;
    public SystemValidator(
            NetworkConfig config
    ) {
        this.config = config;
        log.info("시스템 검증을 수행합니다");

        //명령어 설치 확인
        checkCommandInstalled("tshark");
        checkCommandInstalled("arp-scan");
        checkCommandInstalled("iwconfig");
        checkCommandInstalled("nmcli");

        //네트워크 인터페이스 확인
        List<NetworkInterface> system = getSystemNetworkInterfaces();
        List<NetworkInterface> setting = config.getNetworkInterfaces();
        log.info("\n시스템 네트워크 인터페이스 - \n{}",
                system.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));
        log.info("\n설정된 네트워크 인터페이스 - \n{}",
                setting.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));
        checkNetworkInterfaces(system, setting);
        log.info("시스템 검증 완료");
    }

    //정기적으로 시스템 상태를 검사합니다.
    @Scheduled(fixedDelay = 30000)
    private void checkRegularly(){
        try {
            checkNetworkInterfaces(getSystemNetworkInterfaces(), this.config.getNetworkInterfaces());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private void checkCommandInstalled(String command) {
        List<String> results = new TransientProcess("which " + command).resultList();
        if (results.isEmpty() || !results.get(0).contains("/")) {
            throw new IllegalStateException(command + "가 설치되지 않았습니다.");
        }
    }

    //세팅된 NetworkInterface들이 시스템에 존재하는 NetworkInterface인지 확인
    private void checkNetworkInterfaces(List<NetworkInterface> system, List<NetworkInterface> setting) {
        List<NetworkInterface> unmatchedNIs = setting.stream()
                .filter(ni -> !system.contains(ni))
                .toList();

        if (!unmatchedNIs.isEmpty()) {
            throw new IllegalStateException(
                    "시스템에 존재하지 않거나 상태가 올바르지 않습니다: \n" +
                    unmatchedNIs.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining("\n"))
            );
        }
    }

    private List<NetworkInterface> getSystemNetworkInterfaces() {
        List<NetworkInterface> interfaces = new ArrayList<>();

        List<String> iwconfigOutput = new TransientProcess("iwconfig").resultList();

        String currentName = null;
        String currentEssid = null;
        String currentMode = null;

        for (String line : iwconfigOutput) {
            line = line.trim();
            // 인터페이스 이름 감지 (인터페이스 정보 나오기 시작)
            if (!line.startsWith(" ") && (line.contains("IEEE 802.11") || line.contains("unassociated"))) {
                if (currentName != null) {
                    // 첫 인터페이스가 아니면 모아둔 이전 인터페이스의 정보 저장
                    interfaces.add(new NetworkInterface(currentName, currentEssid, currentMode));
                }
                // 새 인터페이스 정보 모으기 & 초기화
                currentName = line.split("\\s+")[0];
                if (line.contains("ESSID:"))
                    currentEssid = line.split("ESSID:")[1].split("\\s+")[0].replace("\"", "").trim();
                else
                    currentEssid = "";
                currentMode = null; // 초기화
            }
            // Mode 추출
            if (line.startsWith("Mode:")) {
                currentMode = line.split("Mode:")[1].split("\\s")[0].trim().toLowerCase();
            }
        }

        // 마지막 인터페이스 추가
        if (currentName != null) {
            interfaces.add(new NetworkInterface(currentName, currentEssid, currentMode));
        }
        return interfaces;
    }
}


/*
//샘플 iwconfig
List<String> iwconfigOutput = List.of(
        "lo        no wireless extensions.",
        "",
        "eth0      no wireless extensions.",
        "",
        "default_wlan  IEEE 802.11  ESSID:\"JNU\"",
        "          Mode:Managed  Frequency:5.18 GHz  Access Point: 50:E4:E0:B9:78:D0",
        "          Bit Rate=24 Mb/s   Tx-Power=31 dBm",
        "          Retry short limit:7   RTS thr:off   Fragment thr:off",
        "          Power Management:on",
        "          Link Quality=69/70  Signal level=-41 dBm",
        "          Rx invalid nwid:0  Rx invalid crypt:0  Rx invalid frag:0",
        "          Tx excessive retries:12  Invalid misc:0   Missed beacon:0",
        "",
        "usb_wlan1  IEEE 802.11  ESSID:\"ECONO_5G\"",
        "          Mode:Managed  Frequency:5.745 GHz  Access Point: 5A:86:94:45:F0:74",
        "          Bit Rate=866.7 Mb/s   Tx-Power=23 dBm",
        "          Retry short limit:7   RTS thr:off   Fragment thr:off",
        "          Power Management:on",
        "          Link Quality=70/70  Signal level=0 dBm",
        "          Rx invalid nwid:0  Rx invalid crypt:0  Rx invalid frag:0",
        "          Tx excessive retries:0  Invalid misc:20   Missed beacon:0",
        "",
        "monitor_wlan  IEEE 802.11b  ESSID:\"\"  Nickname:\"WIFI@RTL8814AU\"",
        "          Mode:Monitor  Frequency:2.462 GHz  Access Point: Not-Associated",
        "          Sensitivity:0/0",
        "          Retry:off   RTS thr:off   Fragment thr:off",
        "          Power Management:off",
        "          Link Quality:0  Signal level:0  Noise level:0",
        "          Rx invalid nwid:0  Rx invalid crypt:0  Rx invalid frag:0",
        "          Tx excessive retries:0  Invalid misc:0   Missed beacon:0"
);
 */