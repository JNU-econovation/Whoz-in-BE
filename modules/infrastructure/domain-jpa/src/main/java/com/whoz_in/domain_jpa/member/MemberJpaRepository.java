package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.model.SocialProvider;
import java.util.Optional;
import java.util.UUID;
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
    return jpaRepository.existsBySocialProviderAndSocialId(socialProvider, socialId);
  }

  @Override
  public Optional<Member> findByName(String name) {
    Optional<MemberEntity> entity = jpaRepository.findByName(name);
    return entity.map(converter::to);
  }

  @Override
  public Optional<Member> findByMemberId(MemberId id) {
    UUID memberId = id.id();
    Optional<MemberEntity> entity = jpaRepository.findById(memberId);
    return entity.map(converter::to);
  }
}
