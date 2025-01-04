package com.whoz_in.network_api.common;

import com.whoz_in.network_api.common.process.TransientProcess;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//iwconfig의 출력을 파싱하여 네트워크 인터페이스들을 반환함
//prod 환경에선 iwconfig가 설치되어있을 것을 SystemValidator를 통해 보장한다.
@Profile("prod")
@Component
public final class IwconfigNetworkInterfaces implements SystemNetworkInterfaces {

    //최신 정보를 가져온다.
    public List<NetworkInterface> getLatest() {
        List<NetworkInterface> interfaces = new ArrayList<>();

        List<String> iwconfigOutput = new TransientProcess("iwconfig").resultList();

        String currentName = null;
        String currentEssid = null;
        String currentMode = null;

        for (String line : iwconfigOutput) {
            line = line.trim();
            // 인터페이스 이름 감지 (인터페이스 정보 나오기 시작)
            if (!line.startsWith(" ") && (line.contains("IEEE 802.11") || line.contains(
                    "unassociated"))) {
                if (currentName != null) {
                    // 첫 인터페이스가 아니면 모아둔 이전 인터페이스의 정보 저장
                    interfaces.add(new NetworkInterface(currentName, currentEssid, currentMode));
                }
                // 새 인터페이스 정보 모으기 & 초기화
                currentName = line.split("\\s+")[0];
                if (line.contains("ESSID:"))
                    currentEssid = line.split("ESSID:")[1].split("\\s+")[0].replace("\"", "")
                            .trim();
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
