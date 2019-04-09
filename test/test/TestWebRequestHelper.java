package test;

import java.io.IOException;

import com.hirohiro716.net.web.WEBRequest;

@SuppressWarnings({ "all"})
public class TestWebRequestHelper {

    public static void main(String[] args) {
        WEBRequest helper = new WEBRequest("https://www.aketa.jp/aketadmin/");
        
        
        
        helper.putParam("login_id", "hiro");
        helper.putParam("password", "Hiro6295");
        try {
            System.out.println(helper.requestPostResult());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
