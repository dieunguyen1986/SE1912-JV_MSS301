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

    /**
     * Case 1: Đơn đã được tiếp nhận, CV đang được xử lý.
     * Gửi khi job-service xác nhận còn slot (job.slot.reserved).
     */
    public void sendApplicationReceived(String toEmail, String candidateName,
                                         String jobTitle, int currentCount, int maxApplicants) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@talenthub.com");
        message.setTo(toEmail);
        message.setSubject("TalentHub: Đơn ứng tuyển đã được tiếp nhận");
        message.setText(
                "Xin chào " + candidateName + ",\n\n"
                        + "Đơn ứng tuyển của bạn cho vị trí \"" + jobTitle + "\" đã được tiếp nhận thành công.\n"
                        + "Số lượng ứng viên hiện tại: " + currentCount + "/" + maxApplicants + "\n\n"
                        + "CV của bạn đang được phân tích và xử lý. "
                        + "Chúng tôi sẽ thông báo kết quả trong thời gian sớm nhất.\n\n"
                        + "Trân trọng,\nTalentHub Recruitment Team"
        );

        mailSender.send(message);
        log.info("Application-received email sent to {}", toEmail);
    }

    /**
     * Case 2: Hết slot — số lượng ứng viên đã đạt tối đa.
     * Gửi khi job-service reject vì applicantCount > maxApplicants (job.slot.rejected).
     */
    public void sendSlotFullRejection(String toEmail, String candidateName, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@talenthub.com");
        message.setTo(toEmail);
        message.setSubject("TalentHub: Đơn ứng tuyển không thể tiếp nhận");
        message.setText(
                "Xin chào " + candidateName + ",\n\n"
                        + "Rất tiếc, đơn ứng tuyển của bạn không thể được tiếp nhận.\n"
                        + "Lý do: " + reason + "\n\n"
                        + "Vui lòng thử lại với các vị trí khác đang tuyển dụng.\n\n"
                        + "Trân trọng,\nTalentHub Recruitment Team"
        );

        mailSender.send(message);
        log.info("Slot-full rejection email sent to {}", toEmail);
    }

    /**
     * Case 3: CV không phù hợp (compensation).
     * Gửi khi cv-parser-service publish cv.parsed.failed.
     */
    public void sendCvNotMatchNotification(String toEmail, String candidateName, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@talenthub.com");
        message.setTo(toEmail);
        message.setSubject("TalentHub: Kết quả xét duyệt CV");
        message.setText(
                "Xin chào " + candidateName + ",\n\n"
                        + "Sau khi xem xét, CV của bạn chưa phù hợp với yêu cầu của vị trí ứng tuyển.\n"
                        + "Chi tiết: " + reason + "\n\n"
                        + "Đơn ứng tuyển của bạn đã được huỷ. "
                        + "Bạn có thể cập nhật CV và thử lại với các vị trí khác.\n\n"
                        + "Trân trọng,\nTalentHub Recruitment Team"
        );

        mailSender.send(message);
        log.info("CV-not-match notification email sent to {}", toEmail);
    }

    /**
     * Case 4: CV phù hợp.
     * Gửi khi cv-parser-service publish cv.parsed.success.
     */
    public void sendCvSuccessNotification(String toEmail, String candidateName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@talenthub.com");
        message.setTo(toEmail);
        message.setSubject("TalentHub: CV của bạn đã được thông qua vòng lọc");
        message.setText(
                "Xin chào " + candidateName + ",\n\n"
                        + "Chúc mừng bạn! CV của bạn đã được phân tích thành công và phù hợp với yêu cầu tuyển dụng.\n"
                        + "Chúng tôi sẽ sớm liên hệ với bạn để hẹn lịch phỏng vấn.\n\n"
                        + "Trân trọng,\nTalentHub Recruitment Team"
        );

        mailSender.send(message);
        log.info("CV success notification email sent to {}", toEmail);
    }
}

