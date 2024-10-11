package com.whoz_in.domain.member.domain;

import com.whoz_in.domain.member.domain.model.Member;

public interface MemberRepository {
  void save(Member member);
}
