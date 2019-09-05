package com.warn.hr.common.config.application.common;

import lombok.Data;
import org.springframework.web.util.UriComponentsBuilder;

public @Data class RestServiceConfig {

    private String protocol;

    private String hostName;

    private Integer port;

    private String urlPrefix;

    private String key;

    public UriComponentsBuilder getUri() {
        return UriComponentsBuilder.newInstance().scheme(getProtocol()).host(getHostName()).port(port).path(getUrlPrefix());
    }

}
