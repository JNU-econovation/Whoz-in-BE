package com.whoz_in.domain.member.domain.model;

import com.whoz_in.domain.member.domain.event.MemberCreated;
import com.whoz_in.domain.shared.domain.AggregateRoot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Member extends AggregateRoot {
  private Long memberId;
  private MemberInfo memberInfo;
  private Auth auth;

  // 팩토리 메서드 create: 사용자로부터 입력을 받아 새로 생성하는 경우
  public static Member create(MemberInfo memberInfo, Auth auth) {
    Member member = Member.builder()
            .memberInfo(MemberInfo.create(memberInfo.getName(), memberInfo.getGeneration(), memberInfo.getPosition()))
//            .auth(AuthInfo.create())
            .build();
    member.register(new MemberCreated());
    return member;
  }

  // 팩토리 메서드 load: db로부터 가져오는 경우
  public static Member load(Long memberId, String name, int generation, Position position) {
    return Member.builder()
            .memberId(memberId)
//            .memberInfo(MemberInfo.create())
//            .Auth(AuthInfo.create())
            .build();
  }


}
