package test;

import com.hirohiro716.Datetime;

@SuppressWarnings({ "all"})
public class TestDatetimeHelper {

    public static void main(String[] args) {

        Datetime helper = new Datetime();

        helper.addDay(365);
        helper.addHour(48);

        System.out.println(helper.toDatetimeString());

        System.out.println(Datetime.stringToDate("2016-11-11"));

    }

}
