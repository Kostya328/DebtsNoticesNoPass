package check.debts.debts_notices.mapper;

import check.debts.debts_notices.model.Debts;
import check.debts.debts_notices.model.EmailLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailLogMapper implements RowMapper<EmailLog> {
    public EmailLog mapRow(ResultSet resultSet, int i) throws SQLException {
        EmailLog emailLog = new EmailLog();
        emailLog.setId(resultSet.getInt("id"));
        emailLog.setUin(resultSet.getString("uin"));
        emailLog.setTime(resultSet.getTimestamp("time"));
        emailLog.setIs_manager(resultSet.getInt("is_manager"));

        return emailLog;
    }
}
