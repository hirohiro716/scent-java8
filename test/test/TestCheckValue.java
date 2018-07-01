package test;

import com.hirohiro716.validate.StringValidator;

@SuppressWarnings({ "all"})
public class TestCheckValue {

    public static void main(String[] args) {

        System.out.println(StringValidator.isDatetime(new java.sql.Date(0)));
        
        System.out.println(TestCheckValue.class.getSimpleName());

    }

}
