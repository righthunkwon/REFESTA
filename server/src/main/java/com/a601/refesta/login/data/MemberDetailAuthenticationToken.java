package com.a601.refesta.login.data;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MemberDetailAuthenticationToken extends AbstractAuthenticationToken {

    private final MemberDetail memberDetail;

    public MemberDetailAuthenticationToken(Collection<? extends GrantedAuthority> authorities, MemberDetail memberDetail1) {
        super(authorities);
        this.memberDetail = memberDetail1;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
