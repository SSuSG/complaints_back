package com.toyproject.complaints.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class Mail {

    private final JavaMailSender emailSender;

    private MimeMessage createMessageForTempPw(String to, String tempPw) throws MessagingException {
        log.info("Mail_createMessageForTempPw -> 임시비밀번호 이메일 작성");

        System.out.println("보내는 대상 : "+ to);
        System.out.println("임시 비밀번호 : "+tempPw);
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("Complaints" + "임시 비밀번호");//제목

        String msgg="";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<h1> 안녕하세요 Complaints입니다.. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 임시 비밀번호를 로그인 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다!<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>임시 비밀번호 입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= tempPw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        //message.setFrom(new InternetAddress("properties email!","Ace"));//보내는 사람

        return message;
    }

    public boolean sendSimpleMessageForTempPw(String to ,String tempPw) throws MailException, MessagingException , IllegalArgumentException{
        log.info("Mail_sendSimpleMessageForTempPw -> 임시 비밀번호 이메일 전송");

        MimeMessage message = createMessageForTempPw(to,tempPw);
        emailSender.send(message);
        return true;
    }

    private MimeMessage createMessage(String to, String authenticationKey) throws MessagingException {
        log.info("Mail_createMessage -> 이메일 인증 이메일 작성");

        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+authenticationKey);
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("PortMIS" + "회원가입 이메일 인증");//제목

        String msgg="";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<h1> 안녕하세요 PortMIS입니다.. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다!<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= authenticationKey+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        //message.setFrom(new InternetAddress("properties email!","Ace"));//보내는 사람
        log.info("UserService : createMessage3");
        return message;
    }

    public String sendSimpleMessage(String to) throws MailException , MessagingException , IllegalArgumentException{
        log.info("Mail_sendSimpleMessage -> 인증 이메일 전송");
        String authenticationKey = createKey();

        MimeMessage message = createMessage(to,authenticationKey);
        emailSender.send(message);
        return authenticationKey;
    }


    public static String createKey() {
        log.info("Mail_createKey -> 랜덤코드 10자리 생성(인증키 or 임시비밀번호 생성)");
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) { // 인증코드 10자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }
}
