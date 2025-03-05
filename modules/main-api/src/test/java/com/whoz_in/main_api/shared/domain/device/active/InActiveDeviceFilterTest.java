package com.whoz_in.main_api.shared.domain.device.active;

import com.whoz_in.domain.device.DeviceRepository;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.device.model.DeviceInfo;
import com.whoz_in.domain.device.model.MacAddress;
import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.network_log.MonitorLogRepository;
import com.whoz_in.main_api.query.device.application.active.view.ActiveDeviceViewer;
import com.whoz_in.main_api.query.member.application.MemberViewer;
import com.whoz_in.main_api.shared.domain.device.DeviceFixture;
import com.whoz_in.main_api.shared.domain.member.MemberFixture;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InActiveDeviceFilterTest {

    @Mock private DeviceRepository deviceRepository;
    @Mock private MemberViewer memberViewer;
    @Mock private MemberRepository memberRepository;
    @Mock private ActiveDeviceViewer activeDeviceViewer;
    @Mock private MonitorLogRepository monitorLogRepository;

    @InjectMocks private InActiveDeviceFilter inActiveDeviceFilter;

    @BeforeEach
    void setUp() {
        // ActiveDevice , Device , MemberConnection , Member 세팅
        Member member = MemberFixture.testMember();
        UUID memberId = member.getId().id();

        Device device = DeviceFixture.testDevice(memberId);
        UUID deviceId = device.getId().id();

        memberRepository.save(member);
        deviceRepository.save(device);
    }

    @Test
    @DisplayName("5분_동안_monitor_log_가_발생하지_않은_기기는_inActive라고_판단한다")
    void judgeTest() {
        // TODO: 테스트 작성
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("이미_inActive인_기기는_pass한다")
    void inActivePass(){
        // TODO: 테스트 작성
        Assertions.assertTrue(true);
    }

}
