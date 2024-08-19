package com.example.itbangmodkradankanbanapi.FirstDataSourceConfiguration.V2;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
        basePackages ={
                "com.example.itbangmodkradankanbanapi.repositories.V2.karban",
                "com.example.itbangmodkradankanbanapi.repositories.V1"
        },
        entityManagerFactoryRef = "firstEntityManagerFactory",
        transactionManagerRef = "firstTransactionManager"
)

public class KarbanDataSourceConfig {
    @Value("${value.datasource.first.url}")
    private String url;

    @Value("${value.datasource.first.username}")
    private String username;

    @Value("${value.datasource.first.password}")
    private String password;

    @Primary
    @Bean(name = "firstDataSource")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Primary
    @Bean(name = "firstEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(
            @Qualifier("firstDataSource") DataSource firstDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(firstDataSource);
        em.setPackagesToScan("com.example.itbangmodkradankanbanapi.entities.V2.karban","com.example.itbangmodkradankanbanapi.entities.V1");
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

