package com.whoz_in.infra.jpa.member;

import org.springframework.stereotype.Repository;

@Repository
public class MemberJpaAdapter implements MemberRepository{

  @Override
  public void save() {
    System.out.println("1");
  }
}
