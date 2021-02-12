package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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


    // 이메일 작성하는 메소드와 보내는 메소드 분리하기
    // 목표: 메일 발송은 send mail 로 놔두고 필요에 따라 이메일 내용만 바꾸어쓸 수 있도록 만들기
    public void sendEmailWithNewPassword(String to, String newPassword) {

        String title = "<Hello World> 회원님의 임시비밀번호 안내입니다.";
        String content = "회원님의 임시 비밀번호는 " + newPassword + " 입니다. 로그인 후 비밀번호를 다시 변경해주세요.";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(title);
            messageHelper.setText(content);
            mailSender.send(message);

        } catch (MessagingException e) {
            // MimeMessageHelper 가 제대로 생성되지 않는 예외가 발생하면 복구가능할까? 불가능 시 전환, 가능 시 최대한 복구 해보자
            throw new RuntimeException(e);
        }
    }

}
