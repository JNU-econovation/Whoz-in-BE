package com.whoz_in.domain.member;

import com.whoz_in.domain.member.model.Member;

public interface MemberRepository {
  void save(Member member);
}
