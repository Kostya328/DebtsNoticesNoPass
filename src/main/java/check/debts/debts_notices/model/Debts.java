package check.debts.debts_notices.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debts {
  //Psnmast
  private String psn;
  private String sname;
  private String fname;
  private String parname;
  private String email;

  //Debts
  private String reason;
  private String ordinance;
  private java.sql.Timestamp dbtdte;
  private java.sql.Timestamp ofndte;
  private String sum;
  private String sumHalf;
  private java.sql.Timestamp paytodte;
  private java.sql.Timestamp paytoHalf;
  private String regnum;
  private String place;
}
