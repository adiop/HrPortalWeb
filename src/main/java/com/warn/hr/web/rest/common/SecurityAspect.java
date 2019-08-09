package com.warn.hr.web.rest.common;

import com.warn.hr.persistence.entity.hr.User;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SecurityAspect {

    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void verifyRequest() {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();

        if(!(isUser(request) || isUserAdmin(request))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    public Boolean isUserInRole(HttpServletRequest request, String role) {
        return getUser(request).getAuthorities().stream().anyMatch(a -> a.getAuthority().replaceAll("ROLE_WARNNT\\\\", "").equalsIgnoreCase(role));
    }

    public User getUser(HttpServletRequest request) {
        return User.of(((waffle.spring.WindowsAuthenticationToken) request.getUserPrincipal()).getName(),
                ((waffle.spring.WindowsAuthenticationToken) request.getUserPrincipal()).getAuthorities());
    }

    public Boolean isUserAdmin(HttpServletRequest request) {
        return isUserInRole(request, "HR PORTAL ADMINS");
    }

    public Boolean isUser(HttpServletRequest request) {
        return isUserInRole(request, "HR PORTAL");
    }

}
