package com.whoz_in.application.member;


import com.whoz_in.application.shared.Handler;
import com.whoz_in.application.shared.query.QueryHandler;

@Handler
public class MemberGetHandler extends QueryHandler<MemberGet, MemberResponse> {

  @Override
  public MemberResponse handle(MemberGet query) {return new MemberResponse();}

}
