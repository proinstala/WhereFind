package io.proinstala.wherefind.api.infraestructure.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.proinstala.wherefind.shared.config.AppSettings;
import io.proinstala.wherefind.shared.dtos.EmailSettingsDTO;


public class Email {


    public static boolean enviarEmail(String emailDestinatario, String asunto, String texto)
    {
        // Se define la variable para guardar las propiedades
        Properties props = new Properties();

        // Se obtiene la configuración del email
        EmailSettingsDTO  emailSettingsDTO = AppSettings.getEmailSettings();

        // Si no hay configuración, devuelve false
        if (emailSettingsDTO == null)
        {
            return false;
        }

        // Servidor SMTP
        props.put("mail.smtp.host", emailSettingsDTO.getSmtpHost());

        // Se requiere identificación
        props.put("mail.smtp.auth", emailSettingsDTO.getSmtpAuth());

        // Configuración segura
        props.put("mail.smtp.starttls.enable", emailSettingsDTO.getSmtpStarttlsEnable());

        // Configuración del puerto
        props.put("mail.smtp.port", emailSettingsDTO.getSmtpPort());

        // Se crea una nueva sesión con los datos de conexión y datos de la cuenta
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(emailSettingsDTO.getEmail(), emailSettingsDTO.getPassword());
            }
        });

        try
        {
            //compone el mensaje
            Message message = new MimeMessage(session);

            // Añade la dirección de quien lo envía
            message.setFrom(new InternetAddress(emailSettingsDTO.getEmail()));

            // Añade la dirección del destinatario
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinatario));

            // Añade la dirección para la copia oculta
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailSettingsDTO.getEmail()));

            //asunto
            message.setSubject(asunto);

            //cuerpo del mensaje en html
            message.setContent(texto, "text/html");

            //envía el mensaje
            Transport.send(message);
        }
        catch (MessagingException ex)
        {
            ex.printStackTrace();

            // Devuelve false en caso de error
            return false;
        }

        // Devuelve true por considerarse que se ha enviado
        return true;
    }
}
