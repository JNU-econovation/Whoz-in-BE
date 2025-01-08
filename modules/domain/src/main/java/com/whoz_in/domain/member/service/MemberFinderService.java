package com.whoz_in.domain.member.service;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MemberFinderService {
    private final MemberRepository memberRepository;
    public void mustExist(MemberId memberId) {
        if (!memberRepository.existsByMemberId(memberId))
            throw new NoMemberException();
    }
}
