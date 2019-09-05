package com.warn.hr.common.config.application.common;

public interface ApplicationEnvironmentConfig {

    public DbConnectionConfig getDataBaseConfig();

    public RestServiceConfig getActiveDirectoryRestService();

    public RestServiceConfig getTaskRestService();

    public RestServiceConfig getTicketRestService();

}
