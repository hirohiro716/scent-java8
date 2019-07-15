package test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.hirohiro716.RudeArray;

@SuppressWarnings({ "all"})
public class RudeArrayTest {

    public static void main(String[] args) throws Exception {
        
        RudeArray array = new RudeArray();
        array.put(1, "a");
        
        byte[] bytes = array.toSerialize();
        
        array = RudeArray.desirialize(bytes);
        
        HashMap<String, Integer> hashMap = array.getLinkedHashMap();
        Object i = hashMap.get(1);
        
        System.out.println(i);

    }

}
