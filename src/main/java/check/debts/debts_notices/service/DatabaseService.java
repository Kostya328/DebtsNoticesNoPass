package check.debts.debts_notices.service;

import check.debts.debts_notices.AppConfig;
import check.debts.debts_notices.mapper.DebtsMapper;
import check.debts.debts_notices.model.Debts;
import check.debts.debts_notices.model.EmailLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public List<Debts> getDebtsList(DataSource dataSource, int brn) {
        List<Debts> debtsList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        List<Debts> list1 = jdbcTemplateMsSql.query(String.format("SELECT d.*, p.* FROM Debts d, Psnmst p, Vclmst v WHERE d.Vcl = v.Vcl AND d.Brn = v.Brn AND p.Psn = v.Psn1 AND d.Brn = %s AND d.Paytodte > '%s' AND d.Rcdsts = 0", brn, sdf.format(new Date())), new DebtsMapper());

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT d.*, p.* FROM Debts d, Psnmst p, Vclmst v WHERE d.Vcl = v.Vcl AND d.Brn = v.Brn AND p.Psn = v.Psn1 AND d.Brn = ? AND d.Paytodte > ? AND d.Rcdsts = 0");

            stmt.setInt(1, brn);
            stmt.setString(2, sdf.format(new Date()));
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
                debtsList.add(new Debts(rs.getString("psn"), rs.getString("sname"),rs.getString("fname"),
                        rs.getString("parname"), rs.getString("email"), rs.getString("reason"),
                        rs.getString("ordinance"), rs.getTimestamp("dbtdte"), rs.getTimestamp("ofndte"),
                        rs.getString("sum"), rs.getString("sumHalf"), rs.getTimestamp("paytodte"),
                        rs.getTimestamp("paytoHalf"), rs.getString("regnum"), rs.getString("place")));
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return debtsList;
    }

    public List<EmailLog> emailLogList(DataSource dataSource, String uin) {
        List<EmailLog> emailLogList = new ArrayList<>();
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM email_log WHERE uin = ?");

            stmt.setString(1, uin);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                emailLogList.add(new EmailLog(
                        rs.getLong("id"),
                        rs.getString("uin"),
                        rs.getTimestamp("time"),
                        rs.getInt("is_manager"))
                );
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailLogList;
    }

    public int wrightEmailLog(DataSource dataSource, EmailLog emailLog) {
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO email_log (uin, time, is_manager) VALUES (?, ?, ?)");

            stmt.setString(1, emailLog.getUin());
            stmt.setTimestamp(2, emailLog.getTime());
            stmt.setInt(3, emailLog.getIs_manager());

            int res = stmt.executeUpdate();

            con.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
