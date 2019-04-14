package test;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.hirohiro716.email.EmailTransmitter;
import com.hirohiro716.email.EmailTransmitter.ReceiverType;

@SuppressWarnings("all")
public class TestMailHelper {

    public static void main(String[] args) {
        
        try {

            EmailTransmitter mail = new EmailTransmitter();
            mail.setMyEmailAddress("not.alter@gmail.com");
            mail.setHost("smtp.gmail.com");
            mail.setUser("not.alter@gmail.com");
            mail.setPassword("55838553");
            mail.addReceiverEmailAddress("hirohiro716@gmail.com", ReceiverType.TO);
            mail.setPortNumber(465);
            mail.setDebug(true);
            mail.setEnableTLS(true);
            mail.send("手酢と3", "テスト めっせーじ ｻﾝ");
            
            System.out.println("ok");
            
        } catch (Exception exception) {
            
            exception.printStackTrace();
            
        }
        
    }

}
