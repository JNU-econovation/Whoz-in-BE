package com.whoz_in.network_api.common.network_interface.mac;

import com.whoz_in.network_api.common.network_interface.NetworkAddress;
import com.whoz_in.network_api.common.network_interface.NetworkAddressResolver;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
public class StubNetworkAddressResolver implements NetworkAddressResolver {
    @Override
    public Map<String, NetworkAddress> resolve() {
        Map<String, NetworkAddress> networkAddresses = new HashMap<>();

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // 루프백 인터페이스 및 비활성화된 인터페이스 무시
                }

                List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();

                for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                    InetAddress address = interfaceAddress.getAddress();

                    if (address.isLoopbackAddress() || address.getHostAddress().contains(":")) {
                        continue; // IPv6 및 루프백 주소 제외
                    }

                    String interfaceName = networkInterface.getName();
                    String ipAddress = address.getHostAddress();
                    String gateway = getGatewayAddress();

                    networkAddresses.put(interfaceName, new NetworkAddress(ipAddress, gateway));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return networkAddresses;
    }

    private String getGatewayAddress() {
        return "192.168.0.1";
    }
}
