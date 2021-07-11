//package com.patrickreplogle.bugtracker.config;
//
//import com.patrickreplogle.bugtracker.util.Constants;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
///**
// * Configures which database we are using based on a property in application.properties
// */
//@Configuration
//public class DataSourceConfig
//{
//    /**
//     * A config var for the database link - defaults to nothing
//     */
//    @Value("${spring.datasource.url:}")
//    private String dbURL;
//
//    /**
//     * The actual datasource configuration
//     *
//     * @return the datasource to use
//     */
//    @Bean
//    public DataSource dataSource()
//    {
//        if (System.getenv(Constants.RUNTIME_ENV) == "production")
//        {
//            // Assume Heroku
//            HikariConfig config = new HikariConfig();
//            config.setDriverClassName("org.postgresql.Driver");
//            config.setJdbcUrl(dbURL);
//            return new HikariDataSource(config);
//        } else
//        {
//            // Assume H2
//            String myURLString = "jdbc:h2:mem:testdb";
//            String myDriverClass = "org.h2.Driver";
//            String myDBUser = "sa";
//            String myDBPassword = "";
//
//            return DataSourceBuilder.create()
//                    .username(myDBUser)
//                    .password(myDBPassword)
//                    .url(myURLString)
//                    .driverClassName(myDriverClass)
//                    .build();
//        }
//    }
//}
