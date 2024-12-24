package com.whoz_in.main_api.command.member.presentation;

import com.whoz_in.main_api.command.device.application.DeviceAdd;
import com.whoz_in.main_api.shared.presentation.CommandQueryController;
import com.whoz_in.main_api.shared.application.command.CommandBus;
import com.whoz_in.main_api.shared.application.query.QueryBus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController extends CommandQueryController {
  public MemberController(CommandBus commandBus, QueryBus queryBus) {
    super(commandBus,queryBus);
  }
  @GetMapping("/ip")
  public String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    String httpXForwardedFor = request.getHeader("HTTP-X-Forwarded-For");
    String httpXForwardedFor2 = request.getHeader("HTTP_X_Forwarded_For");
    String xRealIp = request.getHeader("X-Real-IP");
    String remoteAddr = request.getRemoteAddr();
    StringBuilder result = new StringBuilder();
    result.append("X-Forwarded-For: ").append(xForwardedFor).append("\n");
    result.append("HTTP-X-Forwarded-For: ").append(httpXForwardedFor).append("\n");
    result.append("HTTP-X-Forwarded-For2: ").append(httpXForwardedFor2).append("\n");
    result.append("X-Real-IP: ").append(xRealIp).append("\n");
    result.append("RemoteAddr: ").append(remoteAddr).append("\n");

    dispatch(new DeviceAdd("11:11:11:11:11:11", "111.111.111.111", "zz"));
    return result.toString();
  }
}
