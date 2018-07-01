package test;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.hirohiro716.mail.MailTransmitter;

@SuppressWarnings("all")
public class TestMailHelper {

    public static void main(String[] args) {
        MailTransmitter mail = new MailTransmitter();
        try {

            mail.setMyMailAddress("not.alter@gmail.com");
            mail.setSmtpHost("smtp.gmail.com");
            mail.setSmtpUser("not.alter@gmail.com");
            mail.setSmtpPassword("55838553");
            mail.setToMailAddress("hirohiro716@gmail.com");
            mail.setSmtpPort(465);
            mail.setEnableTls(true);
            mail.send("test3", "test message 3");

            System.out.println("ok");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
