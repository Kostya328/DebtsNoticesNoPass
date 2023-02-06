package check.debts.debts_notices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class DebtsNoticesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebtsNoticesApplication.class, args);
    }

}
