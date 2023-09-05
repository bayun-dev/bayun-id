package dev.bayun.id.core.exception;

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
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Slf4j
@Component
public class BaseHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {

    @Getter
    @Setter
    private int order;

    public BaseHandlerExceptionResolver() {
        setOrder(Ordered.HIGHEST_PRECEDENCE+10);
    }

    @Override
    public ModelAndView resolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            Object handler, @NonNull Exception ex) {

        log.warn("Resolved [%s]".formatted(ex.getClass()), ex);
        if (ex instanceof BaseErrorResponseException baseErrorResponse) {
            return handleBaseErrorResponse(baseErrorResponse, request, response, handler);
        } else if (ex instanceof ErrorResponse errorResponse) {
            if (ex instanceof NoHandlerFoundException noHandlerFoundException) {
                return handleNoHandlerFoundException(noHandlerFoundException, request, response, handler);
            } else {
                BaseErrorResponseException baseErrorResponse = new BaseErrorResponseException(errorResponse); // TODO types...
                return handleBaseErrorResponse(baseErrorResponse, request, response, handler);
            }
        } else {
            if (ex instanceof BindException bindException) {
                return handleBindException(bindException, request, response, handler);
            }
            if (ex instanceof AccessDeniedException accessDeniedException) {
                return handleAccessDeniedException(accessDeniedException, request, response, handler);
            }
            if (ex instanceof AuthenticationCredentialsNotFoundException authenticationCredentialsNotFoundException) {
                return handleAuthenticationCredentialsNotFoundException(authenticationCredentialsNotFoundException, request, response, handler);
            }
            return handleBaseErrorResponse(new InternalErrorException(ex), request, response, handler);
        }
    }

    private ModelAndView handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException authenticationCredentialsNotFoundException, HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handleBaseErrorResponse(new AuthRestartException(authenticationCredentialsNotFoundException), request, response, handler);
    }

    private ModelAndView handleAccessDeniedException(AccessDeniedException accessDeniedException, HttpServletRequest request, HttpServletResponse response, Object handler) {
        BaseErrorResponse baseErrorResponse;
        if (accessDeniedException instanceof CsrfException || accessDeniedException instanceof org.springframework.security.web.server.csrf.CsrfException) {
            baseErrorResponse = new dev.bayun.id.core.exception.CsrfException(accessDeniedException);
        } else if (accessDeniedException instanceof AuthorizationServiceException){
            baseErrorResponse = new AuthorizationFailedException(accessDeniedException);
        } else {
            baseErrorResponse = new ForbiddenException(accessDeniedException);
        }

        return handleBaseErrorResponse(baseErrorResponse, request, response, handler);
    }

    private ModelAndView handleBindException(BindException bindException, HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handleBaseErrorResponse(new BadRequestParametersException(bindException, bindException.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .toList().toArray(String[]::new)), request, response, handler);
    }

    private ModelAndView handleNoHandlerFoundException(NoHandlerFoundException noHandlerFoundException,
        HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isHtmlAccepted(request, response)) {
            return createHtmlView(HttpStatus.NOT_FOUND.value(), "Page not found", "Nothing to see here. There really isn't");
        }

        return handleBaseErrorResponse(new NotFoundException(noHandlerFoundException), request, response, handler);
    }

    protected ModelAndView handleBaseErrorResponse(BaseErrorResponse baseErrorResponse, HttpServletRequest request,
            HttpServletResponse response, Object handler) {

        response.setStatus(baseErrorResponse.getStatus());
        baseErrorResponse.getHeaders().forEach((name, values) -> values.forEach(value -> response.addHeader(name, value)));

        if (baseErrorResponse.getBody() == null) {
            return new ModelAndView();
        }

        BaseErrorBody body = baseErrorResponse.getBody();
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("ok", baseErrorResponse.isOk());
        modelAndView.addObject("status", body.getStatus());
        modelAndView.addObject("type", body.getType());
        modelAndView.addObject("description", body.getDescription());
        modelAndView.addObject("timestamp", body.getTimestamp());
        modelAndView.addAllObjects(body.getProperties());

        return modelAndView;
    }

    protected ModelAndView createHtmlView(int status, String title, String description) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("status", status);
        modelAndView.addObject("title", title);
        modelAndView.addObject("description", description);
        return modelAndView;
    }

    protected boolean isHtmlAccepted(HttpServletRequest request, HttpServletResponse response) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.GET.name()) && !request.getRequestURI().startsWith("/api");
    }
}
