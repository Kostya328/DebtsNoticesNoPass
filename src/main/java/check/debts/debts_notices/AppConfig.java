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
    @Value("${app.postgres.driver}")
    private String dbDriverPostgres;
    @Value("${app.postgres.url}")
    private String jdbcUrlPostgres;
    @Value("${app.postgres.user}")
    private String dbUserPostgres;
    @Value("${app.postgres.password}")
    private String dbPasswordPostgres;

    @Value("${app.mssql.driver}")
    private String dbDriverMsSql;
    @Value("${app.mssql.url.list}")
    private String jdbcUrlMsSql;
    @Value("${app.mssql.user}")
    private String dbUserMsSql;
    @Value("${app.mssql.password}")
    private String dbPasswordMsSql;


    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriverPostgres);
        dataSource.setUrl(jdbcUrlPostgres);
        dataSource.setUsername(dbUserPostgres);
        dataSource.setPassword(dbPasswordPostgres);

        return dataSource;
    }

    public DataSource mssqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriverMsSql);
        dataSource.setUrl(jdbcUrlMsSql);
        dataSource.setUsername(dbUserMsSql);
        dataSource.setPassword(dbPasswordMsSql);

        return dataSource;
    }


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
