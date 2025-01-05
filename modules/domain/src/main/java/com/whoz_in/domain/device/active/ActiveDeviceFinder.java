package com.whoz_in.domain.device.active;

/**
 * 주기적으로 Active 상태인 Device 들을 찾는 친구
 * void find() : Active 상태인 Device 를 찾아서 ActiveDeviceRepository 에 저장
 * Active 상태인지 판별하는 로직이 들어가 있다.
 */
public interface ActiveDeviceFinder {

    void find();

}
