package com.whoz_in.domain.member;

import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.model.SocialProvider;
import java.util.Optional;

public interface MemberRepository {
  void save(Member member);
  Optional<Member> findByLoginId(String loginId);
  boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
  Optional<Member> findByMemberId(MemberId id);
  Optional<Member> findByName(String name);
  Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
  default Member getByLoginId(String loginId){
    return findByLoginId(loginId).orElseThrow(NoMemberException::new);
  }
  default Member getByName(String name){
    // TODO: 동명이인에 대한 처리, 예외를 던지는 게 좋을까?
    return findByName(name).orElseThrow(NoMemberException::new);
  }
  default Member getByMemberId(MemberId id){
    return findByMemberId(id).orElseThrow(NoMemberException::new);
  }
  default Member getBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId){
    return findBySocialProviderAndSocialId(socialProvider, socialId).orElseThrow(NoMemberException::new);
  }
}
