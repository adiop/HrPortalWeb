package com.warn.hr.business.rest;

import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

public interface _RestClient {

    static <T extends _RestClient> T of(String endpoint, Class<T> tClass) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .retryer(new Retryer.Default())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .target(tClass, endpoint);
    }

}
