package com.warn.hr.business.rest;

import com.warn.hr.persistence.entity.hr.Authentication;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

public interface TokenRestClient {

    @RequestLine("GET /validate-token?auth-token={token}")
    public String isValidToken(@Param("token") String token);

    @RequestLine("GET /auth-info?auth-token={token}")
    public Authentication getAuthenticationInformationByToken(@Param("token") String token);

    public static TokenRestClient of(String endpoint) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .retryer(new Retryer.Default())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .target(TokenRestClient.class, endpoint);
    }

}
