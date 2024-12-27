package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberJpaAdapter implements MemberRepository {
  private final MemberConverter converter;
  private final MemberJpaRepository jpaRepository;

  @Override
  public void save(Member member) {
    MemberEntity memberEntity = converter.from(member);
    jpaRepository.save(memberEntity);
  }
}
