package com.whoz_in.main_api.config.security;

import com.whoz_in.domain.member.model.MemberId;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

//SecurityContextHolder에 저장할 인증 객체로, jwt(access, device register)로 만들어진다.
@RequiredArgsConstructor
public class JwtAuthentication implements Authentication {
    private final MemberId memberId;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public MemberId getPrincipal() {
        return this.memberId;
    }

    @Override
    public String getName(){
        return this.memberId.toString();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials(){
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDetails(){
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated){
        throw new UnsupportedOperationException();
    }
}
