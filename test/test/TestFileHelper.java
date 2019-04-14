package test;

import java.io.File;
import java.io.FilenameFilter;

import com.hirohiro716.file.FileHelper;

@SuppressWarnings("all")
public class TestFileHelper {

    public static void main(String[] args) {
        
        File directory = new File("/home/hiro/Pictures");
        
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".png")) {
                    return true;
                }
                return false;
            }
        };
        
        for (File file: FileHelper.findAllFilesFromDirectory(directory, null)) {
            System.out.println(file.toString());
        }
        
        
        
    }

}
