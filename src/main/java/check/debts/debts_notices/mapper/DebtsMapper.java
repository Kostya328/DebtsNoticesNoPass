package check.debts.debts_notices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import check.debts.debts_notices.model.Debts;
import org.springframework.jdbc.core.RowMapper;

public class DebtsMapper implements RowMapper<Debts> {
    public Debts mapRow(ResultSet resultSet, int i) throws SQLException {
        Debts debts = new Debts();
        debts.setPsn(resultSet.getString("psn"));
        debts.setSname(resultSet.getString("sname"));
        debts.setFname(resultSet.getString("fname"));
        debts.setParname(resultSet.getString("parname"));
        debts.setEmail(resultSet.getString("email"));

        debts.setReason(resultSet.getString("reason"));
        debts.setOrdinance(resultSet.getString("ordinance"));
        debts.setDbtdte(resultSet.getTimestamp("dbtdte"));
        debts.setOfndte(resultSet.getTimestamp("ofndte"));
        debts.setSum(resultSet.getString("sum"));
        debts.setSumHalf(resultSet.getString("sumHalf"));
        debts.setPaytodte(resultSet.getTimestamp("paytodte"));
        debts.setPaytoHalf(resultSet.getTimestamp("paytoHalf"));
        debts.setRegnum(resultSet.getString("regnum"));
        debts.setPlace(resultSet.getString("place"));

        return debts;
    }
}

