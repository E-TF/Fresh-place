package individual.freshplace.util;

import individual.freshplace.dto.mail.MailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomMailSender {

    private final MailSender mailSender;

    public void sendMail(final MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getAddress());
        message.setSubject(mailRequest.getTitle());
        message.setText(mailRequest.getContent());
        mailSender.send(message);
    }
}
