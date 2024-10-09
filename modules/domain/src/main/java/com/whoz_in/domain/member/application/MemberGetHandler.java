package com.whoz_in.domain.member.application;

import com.whoz_in.domain.shared.domain.bus.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class MemberGetHandler extends QueryHandler<MemberGet, MemberResponse> {

  @Override
  public MemberResponse handle(MemberGet query) {return new MemberResponse();}

}
