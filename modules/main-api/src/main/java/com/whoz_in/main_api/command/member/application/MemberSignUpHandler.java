package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.AuthCredentials;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.service.PasswordEncoder;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.application.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class MemberSignUpHandler extends CommandHandler<MemberSignUp> {
    private final MemberRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public void handle(MemberSignUp cmd) {
        Member member = Member.create(
                cmd.name(), cmd.position(), cmd.generation(),
                AuthCredentials.create(cmd.loginId(), cmd.password(), encoder)
        );
        repository.save(member);
    }
}
