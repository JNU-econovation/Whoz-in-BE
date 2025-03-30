package com.whoz_in.main_api.command.device.application;

import java.util.List;

public record DeviceInfoTempAddRes(
        AdditionStatus status,
        List<String> ssids
) {}
enum AdditionStatus{
    ADDED, // 새로 추가된 것만 포함함 (이미 등록된 것은 반환하지 않음)
    MULTIPLE_CANDIDATES // 후보 로그가 2개 이상인 것을 나타냄 (등록됐다는 것이 아님) -> 사실 이 경우는 예외로 처리하는게 맞는거 같은데..
}
