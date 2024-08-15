package com.example.itbangmodkradankanbanapi.FirstDataSourceConfiguration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.itbangmodkradankanbanapi.user",
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager"
)
public class SecondDataSourceConfig {

    @Bean(name = "secondDataSource")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/user")
                .username("root")
                .password("!Zazaza080a")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            @Qualifier("secondDataSource") DataSource secondDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(secondDataSource);
        em.setPackagesToScan("com.example.itbangmodkradankanbanapi.user");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") EntityManagerFactory secondEntityManagerFactory) {
        return new JpaTransactionManager(secondEntityManagerFactory);
    }

    @Bean(name = "secondJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Qualifier("secondDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

