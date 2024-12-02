package com.whoz_in.log_writer;


import com.whoz_in.log_writer.common.NetworkInterface;
import com.whoz_in.log_writer.common.process.TransientProcess;
import com.whoz_in.log_writer.config.NetworkConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class SystemValidator {

    public SystemValidator(
            @Value("${spring.profiles.active:default}") String profile,
            ConfigurableApplicationContext context,
            NetworkConfig config
    ) {
        context.getBeanFactory().destroyBean(this);
        log.info("시스템 검증을 수행합니다");
        String osName = System.getProperty("os.name").toLowerCase();
        log.info("운영체제 - {}", osName);
        log.info("스프링 프로필 - {}", profile);

        if (!profile.equals("prod") || !osName.contains("nux")){
            //log-writer는 리눅스에 맞춰서 개발되었으므로 검증도 리눅스 기준으로 수행한다.
            //따라서 리눅스가 아니면 개발 환경으로 취급하여 검증을 수행하지 않는다.
            log.info("리눅스가 아니거나 스프링 프로필이 prod가 아니므로 시스템 검증을 수행하지 않습니다.");
            return;
        }

        checkCommandInstalled("tshark");
        checkCommandInstalled("arp-scan");
        checkCommandInstalled("iwconfig");
        checkCommandInstalled("nmcli");

        checkNetworkInterfaces(getNetworkInterfaces(), config.getNetworkInterfaces());
        log.info("시스템 검증 완료");
    }

    private void checkCommandInstalled(String command) {
        List<String> results = new TransientProcess("which " + command).results();
        if (results.isEmpty() || !results.get(0).contains("/")) {
            log.error("{}가 설치되지 않았습니다.", command);
            throw new IllegalStateException(command + "가 설치되지 않았습니다.");
        }
    }

    //세팅된 NetworkInterface들이 시스템에 존재하는 NetworkInterface인지 확인
    private void checkNetworkInterfaces(List<NetworkInterface> system, List<NetworkInterface> setting) {
        log.info("시스템 네트워크 인터페이스 - \n{}",
                system.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));
        log.info("설정된 네트워크 인터페이스 - \n{}",
                setting.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n")));
        if (!system.containsAll(setting)) {
            String e = "설정된 인터페이스가 시스템에 존재하지 않거나 상태가 올바르지 않습니다.";
            log.error(e);
            throw new IllegalStateException(e);
        }
    }

    private List<NetworkInterface> getNetworkInterfaces() {
        List<String> iwconfigOutput = new TransientProcess("iwconfig").results();

        List<NetworkInterface> interfaces = new ArrayList<>();

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