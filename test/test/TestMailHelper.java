package test;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.hirohiro716.email.EmailTransmitter;

@SuppressWarnings("all")
public class TestMailHelper {

    public static void main(String[] args) {
        
        try {

            EmailTransmitter mail = new EmailTransmitter();
            mail.setMyMailAddress("not.alter@gmail.com");
            mail.setSmtpHost("smtp.gmail.com");
            mail.setSmtpUser("not.alter@gmail.com");
            mail.setSmtpPassword("55838553");
            mail.setToMailAddress("hirohiro716@gmail.com");
            mail.setSmtpPortNumber(465);
            mail.setDebug(true);
            mail.setEnableTls(true);
            mail.send("手酢と3", "テスト めっせーじ ｻﾝ");
            
            System.out.println("ok");
            
        } catch (Exception exception) {
            
            exception.printStackTrace();
            
        }
        
    }

}
