package ncu.im3069.demo.app;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.*;

public class SendEmail {
	public String getRandom() {
		Random rd = new Random();
		int num = rd.nextInt(9999999);
		return String.format("%07d", num);
	}
	public boolean sendEmail(String toEmail, String subject, String message) {
		boolean test = false;
		String fromEmail = "book.smtp.server@gmail.com";
		String password = "SAfinalproject0107";
		try {
			Properties pr = new Properties ();
			pr.setProperty("mail.smtp.host", "smtp.gmail.com");
            pr.setProperty("mail.smtp.port", "587");
            pr.setProperty("mail.smtp.auth", "true");
            pr.setProperty("mail.smtp.starttls.enable", "true");
            pr.put("mail.smtp.socketFactory.port", "587");
            pr.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            Session session = Session.getInstance(pr, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });
            MimeMessage mess = new MimeMessage(session);

    		//set from email address
            mess.setFrom(new InternetAddress(fromEmail));
    		//set to email address or destination email address
            mess.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
    		
    		//set email subject
            mess.setSubject(subject, "UTF-8");
            
    		//set message text
            mess.setText(message, "UTF-8");
            //send the message
            Transport.send(mess);
            
            test=true;
			
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
		return test;
	}
}
