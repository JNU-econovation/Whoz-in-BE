package com.whoz_in.api.auth;

import com.whoz_in.api.shared.CommandQueryController;
import com.whoz_in.domain.shared.domain.bus.command.CommandBus;
import com.whoz_in.domain.shared.domain.bus.query.QueryBus;

public class AuthController extends CommandQueryController {
  public AuthController(CommandBus commandBus, QueryBus queryBus) {
    super(commandBus, queryBus);
  }

}
