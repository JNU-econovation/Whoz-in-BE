package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.SocialProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {
  private final MemberConverter converter;
  private final MemberEntityJpaRepository jpaRepository;

  @Override
  public void save(Member member) {
    MemberEntity memberEntity = converter.from(member);
    jpaRepository.save(memberEntity);
  }

  @Override
  public Optional<Member> findByLoginId(String loginId) {
    return jpaRepository.findByLoginId(loginId).map(converter::to);
  }

  @Override
  public boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId) {
    //TODO: 구현
    return true;
  }
}
