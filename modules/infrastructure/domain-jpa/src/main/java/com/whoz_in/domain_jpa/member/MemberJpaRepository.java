package com.whoz_in.domain_jpa.member;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.domain.member.model.SocialProvider;
import com.whoz_in.domain_jpa.badge.BadgeConverter;
import com.whoz_in.domain_jpa.badge.BadgeEntity;
import com.whoz_in.domain_jpa.badge.BadgeEntityRepository;
import com.whoz_in.domain_jpa.badge.BadgeMemberEntity;
import com.whoz_in.domain_jpa.badge.BadgeMemberEntityRepository;
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
  private final BadgeMemberEntityRepository badgeMemberRepo;
  private final BadgeConverter badgeConverter;
  private final BadgeEntityRepository badgeRepo;
  @Override
  public void addBadge(MemberId memberId, BadgeId badgeId) {
    MemberEntity memberEntity = memberRepo.findById(memberId.id()).orElseThrow();
    BadgeEntity badgeEntity = badgeRepo.findById(badgeId.id()).orElseThrow();
    badgeMemberRepo.save(new BadgeMemberEntity(memberEntity,badgeEntity));
  }

  @Override
  public void save(Member member) {
    MemberEntity memberEntity = memberConverter.from(member);
    memberRepo.save(memberEntity);
  }

  @Override
  public Optional<Member> findByLoginId(String loginId) {
    return memberRepo.findByLoginId(loginId).map(memberConverter::to);
  }

  @Override
  public boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId) {
    return memberRepo.existsBySocialProviderAndSocialId(socialProvider, socialId);
  }

  @Override
  public List<Member> findByName(String name) {
    List<MemberEntity> entities = memberRepo.findByName(name);
    return entities.stream().map(memberConverter::to).toList();
  }

  @Override
  public Optional<Member> findByMemberId(MemberId id) {
    UUID memberId = id.id();
    Optional<MemberEntity> entity = memberRepo.findById(memberId);
    return entity.map(memberConverter::to);
  }

  @Override
  public Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId) {
    return memberRepo.findBySocialProviderAndSocialId(socialProvider, socialId).map(memberConverter::to);
  }
}
