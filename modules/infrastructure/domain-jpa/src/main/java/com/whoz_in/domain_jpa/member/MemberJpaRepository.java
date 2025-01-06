package com.whoz_in.domain_jpa.member;

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
  public boolean existsBySocialId(String socialId) {
    return jpaRepository.existsBySocialId(socialId);
  }

  @Override
  public List<Member> findByName(String name) {
    List<MemberEntity> entities = jpaRepository.findByName(name);
    return entities.stream().map(converter::to).toList();
  }

  @Override
  public Optional<Member> findByMemberId(MemberId id) {
    UUID memberId = id.id();
    Optional<MemberEntity> entity = jpaRepository.findById(memberId);
    return entity.map(converter::to);
  }

  @Override
  public Optional<Member> findBySocialId(String socialId) {
    return jpaRepository.findBySocialId(socialId).map(converter::to);
  }
}
