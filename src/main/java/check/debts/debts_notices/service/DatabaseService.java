package check.debts.debts_notices.service;

import check.debts.debts_notices.AppConfig;
import check.debts.debts_notices.mapper.DebtsMapper;
import check.debts.debts_notices.mapper.EmailLogMapper;
import check.debts.debts_notices.model.Debts;
import check.debts.debts_notices.model.EmailLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseService {
    private final AppConfig appConfig;

    @Autowired
    @Qualifier("jdbcTemplateMsSql")
    private JdbcTemplate jdbcTemplateMsSql;

    @Autowired
    @Qualifier("jdbcTemplatePostgres")
    private JdbcTemplate jdbcTemplatePostgres;

    public List<Debts> getDebtsList(int brn) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return jdbcTemplateMsSql.query(String.format(
                "SELECT d.*, p.* FROM Debts d, Psnmst p, Vclmst v WHERE d.Vcl = v.Vcl AND d.Brn = v.Brn AND p.Psn = v.Psn1 AND" +
                " d.Brn = %s AND d.Paytodte > '%s' AND d.Rcdsts = 0", brn, sdf.format(new Date())), new DebtsMapper()
        );
    }

    public List<EmailLog> emailLogList(String uin) {
        return jdbcTemplatePostgres.query(String.format("SELECT * FROM email_log WHERE uin = '%s'", uin), new EmailLogMapper());
    }

    public int wrightEmailLog(EmailLog emailLog) {
        return jdbcTemplatePostgres.update(String.format("INSERT INTO email_log (uin, time, is_manager) VALUES ('%s', '%s', %s)", emailLog.getUin(), emailLog.getTime(), emailLog.getIs_manager()));
    }
}
