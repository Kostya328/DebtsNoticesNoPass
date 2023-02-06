package check.debts.debts_notices.service;

import check.debts.debts_notices.mapper.ConvertHelper;
import check.debts.debts_notices.model.Debts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final NoticesService noticesService;
    private final DatabaseService databaseService;

    @Value("${app.param.brn}")
    private String brn;

    @Scheduled(cron = "${app.cron.expression}", zone = ConvertHelper.MOSCOW_TIME_ZONE)
    public void startMainProcess() {
        List<Debts> debtsList = databaseService.getDebtsList(Integer.parseInt(brn));
        noticesService.startEmailSendingAsync(debtsList);
    }
}
