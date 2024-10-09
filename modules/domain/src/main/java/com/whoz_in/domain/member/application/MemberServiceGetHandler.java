package com.whoz_in.domain.member.application;

import com.whoz_in.domain.shared.domain.bus.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceGetHandler extends QueryHandler<MemberServiceGet, MemberService> {

  @Override
  public MemberService handle(MemberServiceGet query) {return new MemberService();}

}
