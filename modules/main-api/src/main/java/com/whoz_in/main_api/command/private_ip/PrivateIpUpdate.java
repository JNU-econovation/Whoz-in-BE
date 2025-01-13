package com.whoz_in.main_api.command.private_ip;

import com.whoz_in.domain.device.model.IpAddress;
import com.whoz_in.main_api.command.shared.application.Command;
import java.util.Map;

public record PrivateIpUpdate(
        String room,
        Map<String, String> privateIpList //Map<와이파이이름, 아이피>
) implements Command {

    public PrivateIpUpdate {
        privateIpList.values().forEach(IpAddress::create); //아이피 형식 검증
    }
}
