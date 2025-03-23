package com.whoz_in.main_api.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MacAddressUtil {
    public static boolean isFixedMac(String mac) {
        // 구분자는 ":"여야 함
        String delimiter = ":";

        // 첫 번째 옥텟 추출
        String firstOctetStr = mac.split(delimiter)[0];
        int firstOctet = Integer.parseInt(firstOctetStr, 16);

        // 로컬 관리 비트가 0이면 고정(글로벌) MAC 주소, 1이면 랜덤 MAC 주소
        return (firstOctet & 0x02) == 0;
    }
}
