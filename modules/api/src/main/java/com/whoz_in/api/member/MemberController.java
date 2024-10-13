package com.whoz_in.api.member;

import com.whoz_in.api.shared.CommandQueryController;
import com.whoz_in.application.shared.command.CommandBus;
import com.whoz_in.application.shared.query.QueryBus;

public class MemberController extends CommandQueryController {
  public MemberController(CommandBus commandBus, QueryBus queryBus) {
    super(commandBus,queryBus);
  }

}
