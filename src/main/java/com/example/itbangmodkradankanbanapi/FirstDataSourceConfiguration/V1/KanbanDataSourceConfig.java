//package com.example.itbangmodkradankanbanapi.FirstDataSourceConfiguration.V1;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableJpaRepositories(
//        basePackages ={
//                "com.example.itbangmodkradankanbanapi.repositories.V2.karban",
//                "com.example.itbangmodkradankanbanapi.repositories.V1"
//        },
//        entityManagerFactoryRef = "karbanV1EntityManagerFactory",
//        transactionManagerRef = "karbanV1TransactionManager"
//)
//
//public class KanbanDataSourceConfig {
//    @Value("${value.datasource.first.url}")
//    private String url;
//
//    @Value("${value.datasource.first.username}")
//    private String username;
//
//    @Value("${value.datasource.first.password}")
//    private String password;
//
//    @Primary
//    @Bean(name = "karbanV1DataSource")
//    public DataSource karbanV1DataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:mysql://localhost:3306/KarbanV1")
//                .username(username)
//                .password(password)
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "karbanV1EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean karbanV1EntityManagerFactory(
//            @Qualifier("karbanV1DataSource") DataSource karbanV1DataSource) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(karbanV1DataSource);
//        em.setPackagesToScan("com.example.itbangmodkradankanbanapi.entities.V1");
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        return em;
//    }
//    @Primary
//    @Bean(name = "karbanV1TransactionManager")
//    public PlatformTransactionManager karbanV1TransactionManager(
//            @Qualifier("karbanV1EntityManagerFactory") EntityManagerFactory karbanV1EntityManagerFactory) {
//        return new JpaTransactionManager(karbanV1EntityManagerFactory);
//    }
//    @Primary
//    @Bean(name = "karbanV1JdbcTemplate")
//    public JdbcTemplate karbanV1JdbcTemplate(@Qualifier("karbanV1DataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//}
//
