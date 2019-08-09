package com.warn.hr.persistence.entity.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor(staticName = "of")
public @Data class User {

    private String login;

    private Collection<GrantedAuthority> authorities;

}
