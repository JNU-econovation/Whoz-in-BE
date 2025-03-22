package com.whoz_in.main_api.shared.presentation;

import com.whoz_in.main_api.shared.enums.DeviceType;
import com.whoz_in.main_api.shared.utils.RequestDeviceInfo;
import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.classify.DeviceClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

// 요청당 한 번만 User-Agent를 파싱하여 캐싱하고 이후 재사용하므로 중복 파싱 없이 성능 최적화가 가능함.
// 또한 필요한 요청에서만 의존성 주입되므로 불필요한 계산을 줄일 수 있음.
@Component
@RequestScope
public class RequestDeviceInfoContext implements RequestDeviceInfo {
    private final UserAgent userAgent;

    public RequestDeviceInfoContext(HttpServletRequest request, UserAgentParser parser) {
        this.userAgent = parser.parse(request);
    }

    @Override
    public DeviceType getDeviceType() {
        DeviceClass deviceClass = DeviceClass.valueOf(userAgent.getValue("DeviceClass").toUpperCase());
        DeviceType deviceType;
        switch (deviceClass){
            case PHONE -> deviceType = DeviceType.PHONE;
            case DESKTOP -> deviceType = DeviceType.PC;
            case TABLET -> deviceType = DeviceType.TABLET;
            default -> deviceType = DeviceType.ETC;
        }
        return deviceType;
    }

    @Override
    public String getOs() {
        String os = userAgent.getValue("OperatingSystemName");
        return os != null ? os : "Unknown";
    }
}
