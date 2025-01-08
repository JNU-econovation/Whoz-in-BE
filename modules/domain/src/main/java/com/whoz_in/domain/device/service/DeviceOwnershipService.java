package com.whoz_in.domain.device.service;

import com.whoz_in.domain.device.model.Device;
import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DeviceOwnershipService {
    private final MemberRepository memberRepository;

    public void validateOwnership(Device device, MemberId memberId){
        if (device.isOwnedBy(memberId)) return;
        Member deviceOwner = memberRepository.getByMemberId(device.getMemberId());
        throw new IllegalArgumentException("이 기기는 " + deviceOwner.getName() + " 회원의 기기입니다.");
    }
}
