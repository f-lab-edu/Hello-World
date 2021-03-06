package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.MailNotSentException;
import me.soo.helloworld.model.email.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /*
        비밀번호 찾기만 아니라 차후에 다른 내용의 이메일을 보내는데도 재사용할 수 있도록 (ex. 친구추가 요청 알림 등) 이메일에 들어갈 값을 담을
        EmailBase 클래스를 두고 내용에 맞게 객체를 주입받아서 이메일 내용의 변화에는 영향받지 않도록 하는 것이 목표
    */
    public void sendEmail(Email email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(email.getTo());
            messageHelper.setFrom(from);
            messageHelper.setSubject(email.getTitle());
            messageHelper.setText(email.getBody());

            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            throw new MailNotSentException("메일 시스템에 문제가 있어 해당 이메일 발송에 실패하였습니다. 잠시 후에 다시 시도해 주세요.", e);
        }
    }


}
