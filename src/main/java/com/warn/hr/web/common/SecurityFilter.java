package com.warn.hr.web.common;

import com.warn.hr.business.rest.TokenRestClient;
import com.warn.hr.persistence.entity.hr.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class SecurityFilter extends GenericFilterBean {

    public static final String TOKEN_PARAM = "auth-token";

    private TokenRestClient restClient = TokenRestClient.of("http://redzone-1s.warn.com:8088");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        if(Objects.isNull(session.getAttribute(TOKEN_PARAM))) {

            if (hasToken(httpServletRequest) && isValidToken(httpServletRequest)) {
                Authentication authentication = restClient.getAuthenticationInformationByToken(getToken(httpServletRequest));
                httpServletRequest.getSession().setAttribute(TOKEN_PARAM, authentication);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

        }

        chain.doFilter(request, response);

    }

    protected Boolean hasToken(HttpServletRequest request) {
        String token = getToken(request);
        return Objects.nonNull(token) && !token.isEmpty();
    }

    protected Boolean isValidToken(HttpServletRequest request) {

        Boolean isValid = Boolean.FALSE;

        try {
            isValid = "OK".equals(restClient.isValidToken(getToken(request)));
        } catch (Exception e) {
            log.error("An error has occurred while trying to validate the token", e);
        }

        return isValid;
    }

    protected synchronized String getToken(HttpServletRequest request) {

        String token = getTokenFromCookie(request);

        if(token.isEmpty()) {

            if (Objects.isNull(token) || token.isEmpty()) {
                token = getTokenFromParam(request);
            }

            if (Objects.isNull(token) || token.isEmpty()) {
                token = getTokenFromHeader(request);
            }

        }

        return token;

    }

    private synchronized String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(TOKEN_PARAM);
    }

    private synchronized String getTokenFromParam(HttpServletRequest request) {
        return request.getParameter(TOKEN_PARAM);
    }

    private synchronized String getTokenFromCookie(HttpServletRequest request) {

        try {
            return Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(TOKEN_PARAM))
                    .map(c -> c.getValue())
                    .findFirst()
                    .orElseGet(() -> "");
        } catch (Exception e) {
            return "";
        }

    }

}
