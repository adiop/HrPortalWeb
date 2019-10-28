package com.warn.hr.persistence.entity.hr;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "of")
public @Data class Authentication {

    private String token;

    private String authenticationTime;

    private UserAccount userAccount;

}
