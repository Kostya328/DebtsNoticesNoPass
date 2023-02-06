package check.debts.debts_notices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
public class AppConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.mssql")
    public DataSource dataSourceMsSql() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateMsSql")
    public JdbcTemplate jdbcTemplateMsSql(@Qualifier("dataSourceMsSql") DataSource ds) {
        return new JdbcTemplate(ds);
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.postgres")
    public DataSource dataSourcePostgres() {
        return  DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate jdbcTemplatePostgres(@Qualifier("dataSourcePostgres") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
