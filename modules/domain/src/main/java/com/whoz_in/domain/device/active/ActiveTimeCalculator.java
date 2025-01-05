package com.whoz_in.domain.device.active;


/**
 * Active 상태인 Device 들의 누적 접속 시간, 연속 접속 시간을 계산한다.
 */
public interface ActiveTimeCalculator {

    void calculateContinuousTime();
    void calculateTotalTime();

}
