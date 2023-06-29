package check.debts.debts_notices.service;

import check.debts.debts_notices.AppConfig;
import check.debts.debts_notices.model.Debts;
import check.debts.debts_notices.model.EmailLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticesService {
    private final EmailSendingService emailSendingService;
    private final DatabaseService databaseService;
    public boolean inProcess = false;

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final long DAY = 86400000L;
    private final String MANAGER_EMAIL = "***";

    public void sendNotice(Debts debts, String email) {
        String half = debts.getPaytoHalf().getTime() > new Date().getTime() ? String.format("Льготная оплата %s до %s\n", debts.getSumHalf(), sdf.format(debts.getPaytoHalf())) : "";
        String text = String.format(
                "Уважаем(ая)ый %s,\n" +
                "\n" +
                "Мы уведомляем Вас о наличии непогашенных штрафов за нарушение ПДД, вынесенных на закрепленный за Вами а/м гос. номер %s.\n" +
                "\n" +
                "Гос. номер - %s\n" +
                "Дата нарушения - %s\n" +
                "Статья нарушения - %s\n" +
                "Место - %s\n" +
                "Постановление - %s от %s\n" +
                "Штраф - %s руб.\n" +
                "%s" +
                "\n" +
                "Просим Вас оплатить штраф в кротчайшее время.\n" +
                "Для этого можете воспользоваться личном кабинетом «Флит Стар» пройдя по ссылке ***\n" +
                "\n" +
                "\n" +
                "С уважением и наилучшими пожеланиями,\n" +
                "Команда «Флит Стар»",
                debts.getSname() + " " + debts.getFname(),
                debts.getRegnum(),
                debts.getRegnum(),
                sdf.format(debts.getOfndte()),
                debts.getReason(),
                debts.getPlace(),
                debts.getOrdinance(),
                sdf.format(debts.getDbtdte()),
                debts.getSum(),
                half
        );

        emailSendingService.sendEmail(email, "Уведомление о новом штрафе", text);
    }

    boolean sendManager(Debts debts, List<EmailLog> emailLogList) {//timestamp < now-86400000L
        boolean sandedManager = beSandedManager(emailLogList);
        long now = new Date().getTime();

        if(!sandedManager && debts.getPaytodte().getTime() > now && debts.getDbtdte().getTime()+DAY*18 <= now) {
            sendNotice(debts, MANAGER_EMAIL);
            return true;
        } else
            return false;
    }

    boolean sendDriver(Debts debts, List<EmailLog> emailLogList, int tryNumber, int minDaysAfterDbtdte) {
        boolean sandedManager = beSandedManager(emailLogList);
        long timestamp = getLastEmailTS(emailLogList);
        long now = new Date().getTime();

        if(!sandedManager && timestamp < now-DAY*3 && debts.getPaytodte().getTime() > now && emailLogList.size() == tryNumber && debts.getDbtdte().getTime()+DAY*minDaysAfterDbtdte < now) {
            sendNotice(debts, debts.getEmail());
//            sendNotice(debts, MANAGER_EMAIL);
            return true;
        } else
            return false;
    }

    private boolean beSandedManager(List<EmailLog> emailLogList) {
        return emailLogList.stream().anyMatch(x -> Objects.equals(x.getIs_manager(), 1));
    }

    private long getLastEmailTS(List<EmailLog> emailLogList) {
        if(emailLogList.size() > 0)
            return emailLogList.get(emailLogList.size() - 1).getTime().getTime();
        return 0L;
    }

    @Async
    public void startEmailSendingAsync(List<Debts> debtsList) {
        startEmailSending(debtsList);
    }


    public void startEmailSending(List<Debts> debtsList) {
        inProcess = true;
        int emailSandedCount = 0;
        try {
            for (Debts debts : debtsList) {
                List<EmailLog> emailLogList = databaseService.emailLogList(debts.getOrdinance());
                Timestamp now = new Timestamp(new Date().getTime());
                if (sendManager(debts, emailLogList)) {
                    emailSandedCount++;
                    log.info("Почта отправлена менеджеру");
                    databaseService.wrightEmailLog(new EmailLog(0, debts.getOrdinance(), now, 1));
                } else if (sendDriver(debts, emailLogList, 0, 0)) {
                    emailSandedCount++;
                    log.info("Почта отправлена водителю первый раз");
                    databaseService.wrightEmailLog(new EmailLog(0, debts.getOrdinance(), now, 0));
                } else if (sendDriver(debts, emailLogList, 1, 10)) {
                    emailSandedCount++;
                    log.info("Почта отправлена водителю второй раз");
                    databaseService.wrightEmailLog(new EmailLog(0, debts.getOrdinance(), now, 0));
                }
            }
        } finally {
            inProcess = false;
            log.info("Отправка "+emailSandedCount+" писем завершена");
        }
    }
}
