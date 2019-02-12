package test;

import com.hirohiro716.StringConverter;

@SuppressWarnings({ "all"})
public class TestConvertValue {

    public static final void main(String[] args) {

        StringConverter convert = new StringConverter();
        convert.addReplace("a", "b");
        convert.addReplaceTab("c");

        System.out.println(convert.execute("\tあいうえa"));

    }

}
