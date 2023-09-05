package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.auth.SignInToken;
import dev.bayun.id.core.entity.auth.SignUpToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class AuthenticationService {

    @Setter
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Setter
    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Setter
    private JdbcIndexedSessionRepository sessionRepository;

    @Setter
    private AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void authenticate(UsernamePasswordAuthenticationToken token, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(token, "The token must not be null");

        try {
            Authentication auth = authenticationManager.authenticate(token);
            if (auth == null) {
                throw new AuthenticationServiceException("The auth is null");
            }

            SecurityContext securityContext = securityContextHolderStrategy.getContext();
            securityContext.setAuthentication(auth);

            securityContextHolderStrategy.setContext(securityContext);
            securityContextRepository.saveContext(securityContext, request, response);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        securityContextHolderStrategy.clearContext();
        securityContextRepository.saveContext(securityContextHolderStrategy.createEmptyContext(), request, response);
    }
}
