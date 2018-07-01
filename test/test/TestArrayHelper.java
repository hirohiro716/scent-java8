package test;

import java.util.Arrays;

import com.hirohiro716.ArrayHelper;

@SuppressWarnings("all")
public class TestArrayHelper {

    public static void main(String[] args) {
        Object[] a = new Integer[] {1, 2, 3};
        Object[] b = new Object[] {"a4", "b5", "c6"};
        try {
            System.out.println(Arrays.toString(ArrayHelper.merge(a, b)));
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));
        } catch (Exception exception) {
            System.out.println("e");
            exception.printStackTrace();
        }
    }

}
