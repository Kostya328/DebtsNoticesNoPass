package check.debts.debts_notices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSendingService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String sendTo, String header, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(sendTo);
        msg.setFrom("checkdebts@ining.ru");

        msg.setSubject(header);
        msg.setText(body);

        javaMailSender.send(msg);
    }
}
