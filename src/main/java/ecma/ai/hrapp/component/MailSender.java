package ecma.ai.hrapp.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailSender {

    @Autowired
    JavaMailSender mailSender;

    public boolean send(String to, String text) throws MessagingException {

        String from = "email@gmail.com";//FAKE EMAIL
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setSubject("Information");
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(text);
        mailSender.send(mimeMessage);
        return true;
    }

    //EMAIL VERIFICATION
    public boolean mailTextAddStaff(String email, String code, String pass) throws MessagingException {
        String link = "http://localhost:80/api/user/verifyEmail?email=" + email + "&code=" + code;

        String text = link + "VERIFY YOUR EMAIL\n" +
                "**YOUR PASSWORD: " + pass + "**";
        return send(email, text);
    }

    //NEW TASK NOTIFICATION EMAIL
    public boolean mailTextAddTask(String email, String taskName) throws MessagingException {
        String text = "YOU ARE GIVEN A NEW TASK CALLED " + taskName;

        return send(email, text);
    }

    //TASK COMPLETE NOTIFICATION EMAIL
    public boolean mailTextCompleteTask(String email,String taskTaker, String taskName) throws MessagingException {
        String text = taskTaker+" HAS COMPLETED THE TASK CALLED "+taskName;

        return send(email, text);
    }

}
