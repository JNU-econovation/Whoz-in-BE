package com.whoz_in.main_api.query.open_api.member.presentation;

import static com.whoz_in.main_api.shared.presentation.HttpRequestIdentifier.OPEN_API_PREFIX;

import com.whoz_in.main_api.query.open_api.member.application.by_date.MembersByDate;
import com.whoz_in.main_api.query.open_api.member.application.by_date.MembersByDateGet;
import com.whoz_in.main_api.query.open_api.member.presentation.docs.MemberQueryApi;
import com.whoz_in.main_api.query.shared.application.QueryBus;
import com.whoz_in.main_api.query.shared.presentation.QueryController;
import com.whoz_in.main_api.shared.presentation.response.CrudResponseCode;
import com.whoz_in.main_api.shared.presentation.response.ResponseEntityGenerator;
import com.whoz_in.main_api.shared.presentation.response.SuccessBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("openApiMemberQueryController")
@RequestMapping(OPEN_API_PREFIX + "/api/v1")
public class MemberQueryController extends QueryController implements MemberQueryApi {
    public MemberQueryController(QueryBus queryBus) {
        super(queryBus);
    }

    @GetMapping("/members")
    @Override
    public ResponseEntity<SuccessBody<MembersByDate>> getMembersByDate(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam(value = "day", required = false) Integer day
    ) {
        MembersByDate response = ask(new MembersByDateGet(year, month, day));
        return ResponseEntityGenerator.success(response, CrudResponseCode.READ);
    }
}
