package com.whoz_in.domain.device.service;

import com.whoz_in.domain.device.exception.InvalidDeviceOwnerException;
import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.domain.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DeviceOwnershipService {
    private final MemberFinderService memberFinderService;

    //기기가 자신의 것이 맞는지 확인합니다.
    public void validateIsMine(Device device, MemberId memberId){
        if (device.isOwnedBy(memberId)) return;
        Member deviceOwner = memberFinderService.find(device.getMemberId());
        throw InvalidDeviceOwnerException.of(deviceOwner.getName());
    }
}
