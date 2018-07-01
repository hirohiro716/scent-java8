package test;

import java.io.IOException;

import com.hirohiro716.web.WEBRequest;

@SuppressWarnings({ "all"})
public class TestWebRequestHelper {

    public static void main(String[] args) {
        WEBRequest helper = new WEBRequest("https://www.aketa.jp/aketadmin/");
        helper.putParam("login_id", "admin");
        helper.putParam("password", "password");
        try {
            System.out.println(helper.requestPostResult());
        } catch (IOException exception) {
        }
    }

}
