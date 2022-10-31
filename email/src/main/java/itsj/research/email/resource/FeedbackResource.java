package itsj.research.email.resource;

import itsj.research.email.config.EmailConfig;
import itsj.research.email.model.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

@RestController
@RequestMapping("/feedback")
public class FeedbackResource {

    @Autowired
    private EmailConfig emailConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackResource.class);

    @PostMapping
    public void sendFeedback(@RequestBody Feedback feedback, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new ValidationException("Feedback is not valid");
        }

        // create a email sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        /*mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());*/

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("from@gmail.com");
        mailSender.setPassword("password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        // create an email instance
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(feedback.getEmail());
        mailMessage.setTo("to@gmail.com");
        mailMessage.setSubject("Feedback from "+feedback.getName());
        mailMessage.setText(feedback.getFeedback());

        LocalDateTime dateTime_before_send_email = LocalDateTime.now();
        LOGGER.info("Start send "+dateTime_before_send_email);
        // send the email
        mailSender.send(mailMessage);
        LocalDateTime dateTime_after_send_email = LocalDateTime.now();
        LOGGER.info("End send "+dateTime_after_send_email);
        Duration duration = Duration.between(dateTime_before_send_email, dateTime_after_send_email);
        long millis = duration.toMillis();
        LOGGER.info("Time consumed by send method of JavaMailSender in milliseconds "+millis);
    }
}
