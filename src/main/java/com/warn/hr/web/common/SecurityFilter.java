package com.warn.hr.web.common;

import com.warn.hr.business.rest.TokenRestClient;
import com.warn.hr.persistence.entity.hr.Authentication;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends GenericFilterBean {

    @NonNull
    private TokenRestClient tokenRestClient;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        if(!isAllowed(httpServletRequest)) {

            if (isValidToken(httpServletRequest) && isValidAuthentication(httpServletRequest)) {
                Authentication authentication = tokenRestClient.getAuthenticationInformationByToken(getToken(httpServletRequest));
                session.setAttribute(Constants.Security.SESSION_AUTH_INFO, authentication);
            } else {
                httpServletResponse.sendRedirect(Constants.Security.ACCESS_DENIED_PATH);
                return;
            }

        }

        chain.doFilter(request, response);

    }

    protected Boolean isValidAuthentication(HttpServletRequest request) {

        Boolean isValid = Boolean.FALSE;

        Authentication authentication = tokenRestClient.getAuthenticationInformationByToken(getToken(request));
        isValid = authentication.getUserAccount()
                .getGroups()
                .stream()
                .anyMatch(g -> Constants.Security.ALLOWED_ROLES.contains(g.getName()));

        return isValid;

    }

    protected Boolean isValidToken(HttpServletRequest request) {

        Boolean isValid = Boolean.FALSE;

        if (hasToken(request)) {

            try {
                isValid = "OK".equals(tokenRestClient.isValidToken(getToken(request)));
            } catch (Exception e) {
                log.error("An error has occurred while trying to validate the token", e);
            }

        }

        return isValid;
    }

    protected Boolean hasToken(HttpServletRequest request) {
        String token = getToken(request);
        return Objects.nonNull(token) && !token.isEmpty();
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
        return request.getHeader(Constants.Security.TOKEN_PARAM);
    }

    private synchronized String getTokenFromParam(HttpServletRequest request) {
        return request.getParameter(Constants.Security.TOKEN_PARAM);
    }

    private synchronized String getTokenFromCookie(HttpServletRequest request) {

        try {
            return Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(Constants.Security.TOKEN_PARAM))
                    .map(c -> c.getValue())
                    .findFirst()
                    .orElseGet(() -> "");
        } catch (Exception e) {
            return "";
        }

    }

    static Boolean isAllowed(HttpServletRequest request) {
        return Objects.nonNull(request.getSession().getAttribute(Constants.Security.SESSION_AUTH_INFO)) ||
                Constants.Security.ALLOWED_PATHS.stream().anyMatch(v -> request.getServletPath().contains(v));
    }

}
