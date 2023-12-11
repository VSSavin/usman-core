package com.github.vssavin.usmancore.spring6.auth;

import com.github.vssavin.usmancore.event.EventType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Main interface for service provides user authentication.
 *
 * @author vssavin on 11.12.2023.
 */
public interface AuthService {

    Authentication authenticate(Authentication authentication);

    Collection<GrantedAuthority> processSuccessAuthentication(Authentication authentication, HttpServletRequest request,
            EventType eventType);

    boolean isAuthenticationAllowed(String ipAddress);

    void processFailureAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception);

    Class<? extends Authentication> authenticationClass();

}
