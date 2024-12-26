package com.whoz_in.main_api.command.member.presentation;

import com.whoz_in.main_api.command.member.application.MemberSignUp;
import com.whoz_in.main_api.shared.application.command.CommandBus;
import com.whoz_in.main_api.shared.application.query.QueryBus;
import com.whoz_in.main_api.shared.presentation.CommandQueryController;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController extends CommandQueryController {
  public MemberController(CommandBus commandBus, QueryBus queryBus) {
    super(commandBus,queryBus);
  }

  @PostMapping("/api/v1/signup")
  public ResponseEntity<SuccessBody<Void>> signup(@RequestBody MemberSignUp request){
    dispatch(request);
    return ResponseEntityGenerator.success( "회원가입 완료", HttpStatus.CREATED);
  }
}
