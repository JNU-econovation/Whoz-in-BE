package com.whoz_in.main_api.config.security.oauth2;

import com.whoz_in.domain.member.model.MemberId;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

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
