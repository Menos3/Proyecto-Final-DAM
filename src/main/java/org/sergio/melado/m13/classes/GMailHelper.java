package org.sergio.melado.m13.classes;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GMailHelper {

    private static final String SMTP_USER = "pepejoselin3@gmail.com"; // Remitent
    private static final String SMTP_PROTO = "smtp";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PASSWD = "qwer#ASDF#zxcv#1234"; // Contrasenya del compte del remitent
    private static final String SMTP_PORT = "587";

    /**
     * Envia un coreu utilitzant el servidor de correu smtp de Google
     * El format del missatge és text pla.
     *
     * @param destinatari Destinatari del missatge
     * @param assumpte Assumte del correu
     * @param cos Cos del missatge en format text pla
     */
    public static void send(String destinatari, String assumpte, String cos) {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", SMTP_HOST);  //El servidor SMTP de Google
        props.put("mail.smtp.user", SMTP_USER);
        props.put("mail.smtp.clave", SMTP_PASSWD);    //La contrasenya del compte
        props.put("mail.smtp.auth", "true");    // Utilitzar autenticació mitjançant usuari / contrasenya
        props.put("mail.smtp.starttls.enable", "true"); // Connectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", SMTP_PORT); // El port SMTP segur de Google

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(SMTP_USER)); // Remitent
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatari));   //Destinatari
            message.setSubject(assumpte); // Assumpte
            message.setText(cos); // Cos del missatge
            Transport transport = session.getTransport(SMTP_PROTO);
            transport.connect(SMTP_HOST, SMTP_USER, SMTP_PASSWD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException me) {
            me.printStackTrace();   // Si es produeix algun error
        }
    }
}
