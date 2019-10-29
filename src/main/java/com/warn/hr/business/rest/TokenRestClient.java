package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.hr.Authentication;
import feign.*;

public interface TokenRestClient extends _RestClient {

    @RequestLine("GET /validate-token?auth-token={token}")
    public String isValidToken(@Param("token") String token);

    @RequestLine("GET /auth-info?auth-token={token}")
    public Authentication getAuthenticationInformationByToken(@Param("token") String token);

}
