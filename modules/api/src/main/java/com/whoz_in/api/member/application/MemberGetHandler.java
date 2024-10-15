package com.whoz_in.api.member.application;


import com.whoz_in.api.shared.application.Handler;
import com.whoz_in.api.shared.application.query.QueryHandler;

@Handler
public class MemberGetHandler extends QueryHandler<MemberGet, MemberResponse> {

  @Override
  public MemberResponse handle(MemberGet query) {return new MemberResponse();}

}
