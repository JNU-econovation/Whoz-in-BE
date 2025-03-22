package com.whoz_in.main_api.command.badge.presentation;

import com.whoz_in.main_api.command.badge.application.BadgeAttach;
import com.whoz_in.main_api.command.badge.application.BadgeRegister;
import com.whoz_in.main_api.command.badge.application.SwitchBadgeVisibility;
import com.whoz_in.main_api.command.badge.presentation.docs.BadgeCommandApi;
import com.whoz_in.main_api.command.shared.application.CommandBus;
import com.whoz_in.main_api.command.shared.presentation.CommandController;
import com.whoz_in.main_api.shared.presentation.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.SuccessBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BadgeCommandController extends CommandController implements BadgeCommandApi {
    public BadgeCommandController(CommandBus commandBus) {super(commandBus);}

    @Override
    @PostMapping("/badges")
    public ResponseEntity<SuccessBody<Void>> create(@RequestBody BadgeRegister request) {
        dispatch(request);
        return ResponseEntityGenerator.success( "뱃지 생성 완료", HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/badges/members")
    public ResponseEntity<SuccessBody<Void>> attach(@RequestBody BadgeAttach request) {
        dispatch(request);
        return ResponseEntityGenerator.success( "뱃지 달기 완료", HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/badges/members")
    public ResponseEntity<SuccessBody<Void>> update(@RequestBody SwitchBadgeVisibility request) {
        dispatch(request);
        return ResponseEntityGenerator.success( "뱃지 보여주기 또는 숨기기 변환 완료", HttpStatus.CREATED);
    }
}
