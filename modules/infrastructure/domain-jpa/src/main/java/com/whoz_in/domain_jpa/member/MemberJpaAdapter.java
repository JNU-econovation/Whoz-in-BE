package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJpaAdapter implements MemberRepository {

  @Override
  public void save(Member member) {

  }
}
