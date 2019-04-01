package test;

import java.util.Arrays;

import com.hirohiro716.ArrayHelper;

@SuppressWarnings("all")
public class TestArrayHelper {

    public static void main(String[] args) {
        
        
        Object[] array = new Object[] {4, 5, null, null, "test"};
        
        System.out.println(ArrayHelper.findIndex(array, null));
        
        
        
    }

}
