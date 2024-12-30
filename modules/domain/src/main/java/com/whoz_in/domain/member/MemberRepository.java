package com.whoz_in.domain.member;

import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.SocialProvider;
import java.util.Optional;

public interface MemberRepository {
  void save(Member member);
  Optional<Member> findByLoginId(String loginId);
  boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
  default Member getByLoginId(String loginId){
    return findByLoginId(loginId).orElseThrow(NoMemberException::new);
  }
}
