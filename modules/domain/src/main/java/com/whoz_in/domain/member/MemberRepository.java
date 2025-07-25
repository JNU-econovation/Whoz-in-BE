package com.whoz_in.domain.member;

import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
  void save(Member member);
  boolean existsBySocialId(String socialId);
  boolean existsByMemberId(MemberId memberId);
  Optional<Member> findByMemberId(MemberId id);
  List<Member> findByName(String name);
  Optional<Member> findBySocialId(String socialId);
}
