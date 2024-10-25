package com.whoz_in.api.command.member.presentation;

import com.whoz_in.api.shared.presentation.CommandQueryController;
import com.whoz_in.api.shared.application.command.CommandBus;
import com.whoz_in.api.shared.application.query.QueryBus;

public class MemberController extends CommandQueryController {
  public MemberController(CommandBus commandBus, QueryBus queryBus) {
    super(commandBus,queryBus);
  }

}
