package test;

import java.io.File;

import com.hirohiro716.file.FileHelper;

@SuppressWarnings("all")
public class TestFileHelper {

    public static void main(String[] args) {
        
        File directory = new File("/home/hiro");
        
        
        System.out.println(directory.toURI());
        
        System.out.println(new File(directory.toURI() + "test"));
        
        
        System.out.println(FileHelper.generateOptimizedPathFromURI(directory.toURI()));
        
        
        
    }

}
