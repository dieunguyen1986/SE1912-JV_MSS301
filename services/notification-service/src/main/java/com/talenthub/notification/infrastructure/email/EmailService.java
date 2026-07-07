package com.talenthub.notification.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendAutoReply(String toEmail, String candidateName, String jobTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@talenthub.com");
        message.setTo(toEmail);
        message.setSubject("TalentHub: Xác nhận nhận đơn ứng tuyển");
        message.setText(
                "Xin chào " + candidateName + ",\n\n"
                        + "Chúng tôi đã nhận được đơn ứng tuyển của bạn cho vị trí \""
                        + jobTitle + "\".\n\n"
                        + "Đội ngũ tuyển dụng sẽ xem xét hồ sơ của bạn và phản hồi "
                        + "trong thời gian sớm nhất.\n\n"
                        + "Trân trọng,\nTalentHub Recruitment Team"
        );

        mailSender.send(message);
        log.info("Auto-reply email sent to {}", toEmail);
    }
}
