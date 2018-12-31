package usrportal.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String fromAddress, String toAddress, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(fromAddress);
        javaMailSender.send(mailMessage);
    }
    
    public void sendMimeMail(String fromAddress, String toAddress, String subject, String message){
    	Properties props = System.getProperties();
    	Session session = Session.getInstance(props);
    	MimeMessage mimeMessage = new MimeMessage(session);
    	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    	try {
			helper.setTo(toAddress);
	    	helper.setSubject(subject);
	    	helper.setText(message);
	    	helper.setFrom(fromAddress);
		} catch (MessagingException e) {
			log.error("Email failed to send", e);
		}
        javaMailSender.send(mimeMessage);
    }
}
