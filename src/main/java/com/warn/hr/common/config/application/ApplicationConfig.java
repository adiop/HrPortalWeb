package com.warn.hr.common.config.application;

import com.google.gson.Gson;
import com.warn.hr.business.rest.*;
import com.warn.hr.common.config.application.common.ApplicationEnvironmentConfig;
import com.warn.hr.common.config.application.common.RestServiceConfig;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Application general configurations - See Application.yaml
 */
@Configuration
@EnableTransactionManagement
@EnableWebSocketMessageBroker
public class ApplicationConfig {

    public final ApplicationEnvironmentConfig environmentConfig;

    @Autowired
    public ApplicationConfig(ApplicationEnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }

    /**
     * Ticket Rest Service Configuration
     */
    @Bean
    public String notificationRestServiceKey() {
        return environmentConfig.getNotificationRestService().getKey();
    }

    @Bean
    public NotificationRestClient notificationRestClient() {
        return _RestClient.of(environmentConfig.getNotificationRestService().getUrl(),
                NotificationRestClient.class);
    }

    /**
     * Active Directory Rest Service Configuration
     */
    @Bean
    public String activeDirectoryRestServiceKey() {
        return environmentConfig.getActiveDirectoryRestService().getKey();
    }

    @Bean
    public ActiveDirectoryRestClient activeDirectoryRestClient() {
        return _RestClient.of(environmentConfig.getActiveDirectoryRestService().getUrl(),
                ActiveDirectoryRestClient.class);
    }

    /**
     * Task Rest Service Configuration
     */
    @Bean
    public String taskRestServiceKey() {
        return environmentConfig.getTaskRestService().getKey();
    }

    @Bean
    public TaskRestClient taskServiceRestClient() {
        return _RestClient.of(environmentConfig.getTaskRestService().getUrl(),
                TaskRestClient.class);
    }

    @Bean
    public TicketRestClient ticketServiceRestClient() {
        return _RestClient.of(environmentConfig.getTaskRestService().getUrl(),
                TicketRestClient.class);
    }

    @Bean
    public TaskReportRestClient taskReportRestClient() {
        return _RestClient.of(environmentConfig.getTaskRestService().getUrl(),
                TaskReportRestClient.class);
    }

    /**
     * Security Token Rest Service Configuration
     */
    @Bean
    public TokenRestClient tokenRestClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .retryer(new Retryer.Default())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .target(TokenRestClient.class, environmentConfig.getTokenRestService().getUrl());
    }

    /**
     * Application Data Source Configuration
     */
    @Bean
    public DataSource dataSource() {

        JtdsDataSource dataSource = new JtdsDataSource();
        dataSource.setServerName(environmentConfig.getDataBaseConfig().getHostName());
        dataSource.setUser(environmentConfig.getDataBaseConfig().getUser());
        dataSource.setPassword(environmentConfig.getDataBaseConfig().getPassword());

        return dataSource;
    }


    /**
     * Hibernate Session Factory Configuration
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.warn.hr.persistence.entity.hr");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    /**
     * Hibernate Transaction Manager Configuration
     */
    @Bean
    public HibernateTransactionManager hibernateTransactionManagerHR(SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }

    private final Properties hibernateProperties() {

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");

        return hibernateProperties;

    }

}
