package dev.bayun.id.core.exception;

import dev.bayun.id.core.error.Error;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ResponseBodyHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {

    @Getter
    @Setter
    private int order;

    public ResponseBodyHandlerExceptionResolver() {
        setOrder(Ordered.HIGHEST_PRECEDENCE+10);
    }

    @Override
    public ModelAndView resolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            Object handler, @NonNull Exception ex) {

        if (ex instanceof UsernameNotFoundException usernameNotFoundException) {
            return handleUsernameNotFoundException(usernameNotFoundException, request, response);
        } else if (ex instanceof UsernameOccupiedException usernameOccupiedException) {
            return handleUsernameOccupiedException(usernameOccupiedException, request, response);
        } else if (ex instanceof UsernameUnoccupiedException usernameUnoccupiedException) {
            return handleUsernameUnoccupiedException(usernameUnoccupiedException, request, response);
        } else if (ex instanceof PasswordInvalidException passwordInvalidException) {
            return handlePasswordInvalidException(passwordInvalidException, request, response);
        } else if (ex instanceof AuthenticationCredentialsNotFoundException authenticationCredentialsNotFoundException) {
            return handleAuthenticationCredentialsNotFoundException(authenticationCredentialsNotFoundException, request, response);
        } else if (ex instanceof HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
            return handleHttpRequestMethodNotSupportedException(httpRequestMethodNotSupportedException, request, response);
        } else if (ex instanceof AccessDeniedException accessDeniedException) {
            return handleAccessDeniedException(accessDeniedException, request, response);
        } else if (ex instanceof BindException bindException) {
            return handleBindException(bindException, request, response);
        } else {
            return handleException(ex, request, response);
        }
    }

    private ModelAndView handlePasswordInvalidException(PasswordInvalidException passwordInvalidException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved PasswordInvalidException", passwordInvalidException);
        return createResponse(Error.PASSWORD_INVALID, null, request, response);
    }

    private ModelAndView handleUsernameUnoccupiedException(UsernameUnoccupiedException usernameUnoccupiedException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved UsernameUnoccupiedException", usernameUnoccupiedException);
        return createResponse(Error.USERNAME_UNOCCUPIED, null, request, response);
    }

    private ModelAndView handleUsernameOccupiedException(UsernameOccupiedException usernameOccupiedException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved UsernameOccupiedException", usernameOccupiedException);
        return createResponse(Error.USERNAME_OCCUPIED, null, request, response);
    }

    private ModelAndView handleBindException(BindException bindException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved BindException", bindException);
        return createResponse(Error.INVALID_REQUEST_PARAM,
                Map.of("parameters", bindException.getFieldErrors().stream().map(FieldError::getField).toList()),
                        request, response);
    }

    private ModelAndView handleAccessDeniedException(AccessDeniedException accessDeniedException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved AccessDeniedException", accessDeniedException);
        return createResponse(Error.ACCESS_DENIED, null, request, response);
    }

    private ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved HttpRequestMethodNotSupportedException", httpRequestMethodNotSupportedException);
        return createResponse(Error.INVALID_REQUEST, null, request, response);
    }

    private ModelAndView handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException authenticationCredentialsNotFoundException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved AuthenticationCredentialsNotFoundException", authenticationCredentialsNotFoundException);
        return createResponse(Error.AUTH_RESTART, null, request, response);
    }

    protected ModelAndView handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved UsernameNotFoundException", usernameNotFoundException);
        return createResponse(Error.USERNAME_UNOCCUPIED, null, request, response);
    }

    protected ModelAndView handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.warn("Resolved Exception", e);
        return createResponse(Error.INTERNAL, null, request, response);
    }

    protected ModelAndView createResponse(Error error, Map<String,Object> properties, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(error.getStatus());

        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("ok", false);
        mav.addObject("timestamp", System.currentTimeMillis());
        mav.addObject("status", error.getStatus());
        mav.addObject("type", error.getType());
        mav.addObject("description", error.getDescription());
        mav.addAllObjects(properties);
        return mav;
    }
}
