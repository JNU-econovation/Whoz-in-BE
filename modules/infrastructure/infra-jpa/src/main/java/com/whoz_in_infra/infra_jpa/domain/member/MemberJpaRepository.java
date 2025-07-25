package com.whoz_in_infra.infra_jpa.domain.member;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {
  private final MemberConverter memberConverter;
  private final MemberEntityJpaRepository memberRepo;

  @Override
  public void save(Member member) {
    MemberEntity memberEntity = memberConverter.from(member);
    memberRepo.save(memberEntity);
  }

  @Override
  public boolean existsBySocialId(String socialId) {
    return memberRepo.existsBySocialId(socialId);
  }

  @Override
  public boolean existsByMemberId(MemberId memberId) {
    return memberRepo.existsById(memberId.id());
  }

  @Override
  public List<Member> findByName(String name) {
    List<MemberEntity> entities = memberRepo.findByName(name);
    return entities.stream().map(memberConverter::to).toList();
  }

  @Override
  public Optional<Member> findByMemberId(MemberId id) {
    UUID memberId = id.id();
    Optional<MemberEntity> entity = memberRepo.findOneById(memberId);
    return entity.map(memberConverter::to);
  }

  @Override
  public Optional<Member> findBySocialId(String socialId) {
    return memberRepo.findBySocialId(socialId).map(memberConverter::to);
  }
}
