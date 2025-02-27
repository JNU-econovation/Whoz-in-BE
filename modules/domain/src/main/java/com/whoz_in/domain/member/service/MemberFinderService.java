package com.whoz_in.domain.member.service;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.shared.DomainService;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MemberFinderService {
    private final MemberRepository memberRepository;

    public Member find(MemberId memberId){
        return memberRepository.findByMemberId(memberId).orElseThrow(()->NoMemberException.EXCEPTION);
    }
    public Member findBySocialId(String socialId){
        return memberRepository.findBySocialId(socialId).orElseThrow(()->NoMemberException.EXCEPTION);
    }
    public void mustExist(MemberId memberId) {
        if (!memberRepository.existsByMemberId(memberId))
            throw NoMemberException.EXCEPTION;
    }
}
