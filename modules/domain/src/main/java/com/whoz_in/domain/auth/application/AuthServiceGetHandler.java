package com.whoz_in.domain.auth.application;

import com.whoz_in.domain.shared.domain.bus.query.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public final class AuthServiceGetHandler extends QueryHandler<AuthServiceGet, AuthService> {

  @Override
  public AuthService handle(AuthServiceGet query) {return new AuthService();}
}
