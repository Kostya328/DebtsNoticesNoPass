package check.debts.debts_notices.controller;

import check.debts.debts_notices.AppConfig;
import check.debts.debts_notices.model.Debts;
import check.debts.debts_notices.service.DatabaseService;
import check.debts.debts_notices.service.NoticesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class Controller {
    private final NoticesService noticesService;
    private final DatabaseService databaseService;
    private final AppConfig appConfig;

    @GetMapping("/start/email")
    public ResponseEntity<String> testEmail() {
        if (noticesService.inProcess)
            return ResponseEntity.ok("Отправка писем уже в ппроцессе, повторите запрос позже");

        log.info("Пришел rest запрос на отправку почты");
        List<Debts> debtsList = databaseService.getDebtsList(appConfig.mssqlDataSource(), 4);
        noticesService.startEmailSendingAsync(debtsList);

        return ResponseEntity.ok("Почта отправлена, всего: " + debtsList.size() + " неоплаченых штрафов " + new Date());
    }
}
