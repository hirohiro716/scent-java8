package test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.hirohiro716.RudeArray;
import com.hirohiro716.RudeArray.ValueType;

@SuppressWarnings({ "all"})
public class RudeArrayTest {

    public static void main(String[] args) {
        
        RudeArray array = new RudeArray();
        array.put(1, "a");
        
        HashMap<String, Integer> hashMap = array.getLinkedHashMap();
        Integer i = hashMap.get(1);
        
        System.out.println(i);

    }

}
