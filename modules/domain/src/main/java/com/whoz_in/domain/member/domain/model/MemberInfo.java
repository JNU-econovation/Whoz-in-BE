package com.whoz_in.domain.member.domain.model;

import com.whoz_in.domain.member.domain.event.MemberCreated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MemberInfo {

  private final String name;
  private final int generation;
  private final Position position;

  public static MemberInfo create(String name, int generation, Position position) {
    return new MemberInfo(name, generation, position);
  }
}
