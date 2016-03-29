package io.resourcepool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication
public class Application {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        DataSource datasource = ctx.getBean(DataSource.class);
        System.out.println(datasource);
        if (!(datasource instanceof HikariDataSource)) {
            System.err.println("Wrong datasource type: " + datasource.getClass().getCanonicalName());
            System.exit(-1);
        }
        try {
            Connection connection = datasource.getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT 1");
            if (rs.first()) {
                System.out.println("Connection OK!");
            } else {
                System.out.println("Something is wrong");
            }
            connection.close();
            System.exit(0);
        } catch (SQLException e) {
            System.err.println("FAILED!");
            e.printStackTrace();
            System.exit(-2);
        }
    }
}
