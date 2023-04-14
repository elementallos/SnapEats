package progettoap;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    public EmailSender(){}

    public void sendEmail(String toAddress, String content) {
        // Sender's email ID needs to be mentioned
        String from = "snapeats2023@gmail.com";
        final String username = from;
        final String password = "elugxrfvydnsexcf";

        // Assuming you are sending email through smtp.gmail.com
        String host = "smtp.gmail.com";
        String port = "465";

        Properties props = new Properties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Get the Session object.
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toAddress));

            // Set Subject: header field
            message.setSubject("LISTA ALIMENTI - RIFORNITORE");

            // Set email body content
            message.setContent(content, "text/html");

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
