package com.whoz_in.api.device;

import com.whoz_in.api.shared.CommandQueryController;
import com.whoz_in.api.shared.response.CrudResponseCode;
import com.whoz_in.api.shared.response.ResponseEntityGenerator;
import com.whoz_in.api.shared.response.SuccessBody;
import com.whoz_in.domain.device.application.Example;
import com.whoz_in.domain.device.application.ExampleGet;
import com.whoz_in.domain.device.application.ExamplesGet;
import com.whoz_in.domain.shared.domain.bus.command.CommandBus;
import com.whoz_in.domain.shared.domain.bus.query.QueryBus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class ExampleController extends CommandQueryController {
    public ExampleController(CommandBus commandBus, QueryBus queryBus) {
        super(commandBus, queryBus);
    }

    @GetMapping("/example")
    public ResponseEntity<SuccessBody<Example>> example(){
        Example example = ask(new ExampleGet());
        ResponseEntityGenerator.success(example, "asdf", HttpStatus.BAD_REQUEST);
        return ResponseEntityGenerator.success(example, CrudResponseCode.READ);
    }

    @GetMapping("/examples")
    public ResponseEntity<?> examples(){
        //ExamplesGet이라는 Query만 있고 이 Query를 처리할 Handler는 없기에 예외가 발생합니다.
        ExamplesGet examples = ask(new ExamplesGet());
        return ResponseEntityGenerator.success(examples, CrudResponseCode.READ);
    }
}
