package com.whoz_in.infra.jpa.member;

import com.whoz_in.domain.member.domain.Member;
import com.whoz_in.domain.member.domain.MemberRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJpaAdapter implements MemberRepository {

  @Override
  public void save(Member member) {

  }
}
