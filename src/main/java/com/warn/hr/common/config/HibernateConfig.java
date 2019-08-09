package com.warn.hr.common.config;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:Hibernate.${spring.profiles.active}.properties")
@EnableTransactionManagement
public class HibernateConfig {

    @Value( "${jdbc.hr.server}" )
    private String jdbcHrServer;

    @Value( "${jdbc.hr.user}" )
    private String jdbcHrUser;

    @Value( "${jdbc.hr.password}" )
    private String jdbcHrPassword;

    @Bean("sessionFactoryHR")
    public LocalSessionFactoryBean sessionFactoryHR() {

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSourceHR());
        sessionFactory.setPackagesToScan("com.warn.hr.persistence.entity.hr","com.warn.hr.persistence.entity.task");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    public DataSource dataSourceHR() {

        JtdsDataSource dataSource = new JtdsDataSource();
        dataSource.setServerName(jdbcHrServer);
        dataSource.setUser(jdbcHrUser);
        dataSource.setPassword(jdbcHrPassword);

        return dataSource;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager hibernateTransactionManagerHR(@Qualifier("sessionFactoryHR") SessionFactory sessionFactoryHR){
        return new HibernateTransactionManager(sessionFactoryHR);
    }

    private final Properties hibernateProperties() {

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
//        hibernateProperties.setProperty("hibernate.show_sql", "true");

        return hibernateProperties;

    }



}
