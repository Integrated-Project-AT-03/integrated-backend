package com.example.itbangmodkradankanbanapi.FirstDataSourceConfiguration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.itbangmodkradankanbanapi.repositories.V2",
        entityManagerFactoryRef = "firstEntityManagerFactory",
        transactionManagerRef = "firstTransactionManager"
)


public class FirstDataSourceConfig {
    @Primary
    @Bean(name = "firstDataSource")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/karban")
                .username("root")
                .password("11111111")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
    @Primary
    @Bean(name = "firstEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(
            @Qualifier("firstDataSource") DataSource firstDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(firstDataSource);
        em.setPackagesToScan("com.example.itbangmodkradankanbanapi.entities.V2");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }
    @Primary
    @Bean(name = "firstTransactionManager")
    public PlatformTransactionManager firstTransactionManager(
            @Qualifier("firstEntityManagerFactory") EntityManagerFactory firstEntityManagerFactory) {
        return new JpaTransactionManager(firstEntityManagerFactory);
    }
    @Primary
    @Bean(name = "firstJdbcTemplate")
    public JdbcTemplate firstJdbcTemplate(@Qualifier("firstDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

