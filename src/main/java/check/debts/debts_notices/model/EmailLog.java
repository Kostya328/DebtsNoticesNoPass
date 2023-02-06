package check.debts.debts_notices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailLog {
  private long id;
  private String uin;
  private java.sql.Timestamp time;
  private int is_manager;
}
