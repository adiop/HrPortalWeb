package com.warn.hr.common.config.application;

import com.warn.hr.common.config.application.common.ApplicationEnvironmentConfig;
import com.warn.hr.common.config.application.common.DbConnectionConfig;
import com.warn.hr.common.config.application.common.RestServiceConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Application configurations for staging environment
 */
@Component
@Profile("stg")
@ConfigurationProperties("stg")
public @Data class ApplicationStagingConfig implements ApplicationEnvironmentConfig {

    private DbConnectionConfig dataBaseConfig;

    private RestServiceConfig activeDirectoryRestService;

    private RestServiceConfig taskRestService;

    private RestServiceConfig ticketRestService;

    private RestServiceConfig notificationRestService;

}
