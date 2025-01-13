package com.whoz_in.main_api.query.device.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.whoz_in.main_api.query.shared.application.Response;
import com.whoz_in.main_api.shared.caching.device.TempDeviceInfoStore;
import java.util.Map;


/**
 * 이 클래스는 사용자가 등록해야 할 DeviceInfo들의 상태를 가지고 있다. <br>
 *
 * {@code isAddedPerSsid} 맵은 각 SSID의 임시 등록 여부를 나타낸다. <br>
 * - Key는 사용자가 아직 등록하지 않은 SSID를 의미한다. (db에 없다는 말이다.) <br>
 * - Value가 {@code true}인 경우, 해당 SSID는
 *   {@link TempDeviceInfoStore}에 이미 등록된 것이다. <br>
 *   모두 true라면 기기 등록을 완료할 수 있다. <br>
 */
public record TempDeviceInfosStatus(
    @JsonProperty("status") Map<String, Boolean> isAddedPerSsid
) implements Response {}
