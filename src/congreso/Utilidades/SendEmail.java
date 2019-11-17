/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Utilidades;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author anton
 */
public class SendEmail {

    public void enviar(String congreso,String correo, String nombre, String file, Session sesion) throws UnsupportedEncodingException {

        
        try {

            MimeMessage crunchifyMessage = new MimeMessage(sesion);
            crunchifyMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
            crunchifyMessage.addHeader("format", "flowed");
            crunchifyMessage.addHeader("Content-Transfer-Encoding", "8bit");

            crunchifyMessage.setFrom(new InternetAddress("joserosalio.serrano@unab.edu.sv",
                    "Ing. José Serrano"));
            crunchifyMessage.setReplyTo(InternetAddress.parse("joserosalio.serrano@unab.edu.sv", true));
            crunchifyMessage.setSubject(nombre, "UTF-8");
            crunchifyMessage.setSentDate(new Date());
            crunchifyMessage.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(correo, false));

            // Create the message body part
            String body ="Estimado estudiante "+nombre+","
                    + " descarga, haz captura o imprime la siguiente imagen para poder hacer efectiva tu participación en el "
                    +congreso+".\n "
                    + "<br><br>Nota importante: Este código QR es único y será con el que tendrás acceso al congreso, Break AM, Almuerzo y Break PM.\n<br><br>"
                    + "Saludos Cordiales, <br>Ing. José Serrano";
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");

            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();

            // Valid file location
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("codigo");
            // Trick is to add the content-id header here
            messageBodyPart.setHeader("Content-ID", "image_id");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent("", "text/html");
            multipart.addBodyPart(messageBodyPart);
            crunchifyMessage.setContent(multipart);

            // Finally Send message
            Transport.send(crunchifyMessage);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
