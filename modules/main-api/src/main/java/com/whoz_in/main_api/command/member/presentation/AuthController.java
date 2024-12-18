package com.whoz_in.main_api.command.member.presentation;

import com.whoz_in.main_api.shared.presentation.CommandQueryController;
import com.whoz_in.main_api.shared.application.command.CommandBus;
import com.whoz_in.main_api.shared.application.query.QueryBus;

public class AuthController extends CommandQueryController {
  public AuthController(CommandBus commandBus, QueryBus queryBus) {
    super(commandBus, queryBus);
  }

}
