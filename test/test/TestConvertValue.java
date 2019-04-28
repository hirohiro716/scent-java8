package test;

import com.hirohiro716.StringConverter;

@SuppressWarnings({ "all"})
public class TestConvertValue {

    public static final void main(String[] args) {

        StringConverter convert = new StringConverter();
        convert.addReplaceCr("\r\n");
        convert.addReplaceLf("\r\n");
        
        String value = "\nあ\rあいう\r\nえ\naaa\r";
        
//        System.out.println(convert.execute(value).replaceAll("\r\n", "[crlf]\n"));
        
//        System.out.println(value.replaceAll("\r$|\r([^\n])", "[cr]$1"));
        
        System.out.println(StringConverter.stringToBoolean("TRUE"));
        
        
    }

}
