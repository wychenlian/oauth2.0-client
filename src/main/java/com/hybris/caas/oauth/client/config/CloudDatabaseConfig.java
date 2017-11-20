package com.hybris.caas.oauth.client.config;


import com.hybris.caas.multitenancy.MultiTenantJpaTransactionManager;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * This class uses the database connection information provided in the environment variable VCAP_SERVICES to connect to
 * the database, initializes JPA, and creates a CRUD repository instance. This is done in three steps, as explained
 * below.
 * <p>
 * As this class is registered in the Spring application context,
 * the three methods annotated with {@literal @}Bean also are registered and used to provide bean instances of
 * {@link DataSource}, {@link EntityManagerFactory }, and {@link JpaTransactionManager }, respectively.
 * <p>
 * (Step 3) The @EnableJpaRepositories annotation (of Spring Data JPA) is used to provide a convenient repository, based
 * on JPA (EntityManager, TransactionManager).
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.hybris.caas.shipping.repository"})
@Profile({"stage","prod"})
public class CloudDatabaseConfig extends AbstractCloudConfig {

    /**
     * (Step 1) Parses the local environment variable VCAP_SERVICES (containing cloud information) and provides a
     * DataSource. The superclass {@link AbstractCloudConfig}, part of the Spring Cloud plugin, is used for this.
     */
    @Bean
    public DataSource dataSource() {
        /*
         * Load BasicDbcpPooledDataSourceCreator before TomcatJdbcPooledDataSourceCreator. Also see the following link
         * for a detailled discussion of this issue:
         * https://stackoverflow.com/questions/36885891/jpa-eclipselink-understanding-classloader-issues
         */
        List<String> dataSourceNames = Arrays.asList("BasicDbcpPooledDataSourceCreator",
                "TomcatJdbcPooledDataSourceCreator", "HikariCpPooledDataSourceCreator",
                "TomcatDbcpPooledDataSourceCreator");
        final DataSourceConfig.ConnectionConfig connConfig = new DataSourceConfig.ConnectionConfig(                "currentSchema=caas2");
        final DataSourceConfig dbConfig = new DataSourceConfig(null, connConfig, dataSourceNames);
        return connectionFactory().dataSource(dbConfig);
    }

    /**
     * (Step 2b) Based on an {@link EntityManagerFactory} (provided using the method above), provides a
     * {@link JpaTransactionManager} (another core class of JPA).
     */
    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        MultiTenantJpaTransactionManager multiTenantJpaTransactionManager = new MultiTenantJpaTransactionManager();

        multiTenantJpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return multiTenantJpaTransactionManager;
    }
}