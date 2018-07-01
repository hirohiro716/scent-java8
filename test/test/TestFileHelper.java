package test;

import com.hirohiro716.file.FileHelper;

@SuppressWarnings("all")
public class TestFileHelper {

    public static void main(String[] args) {
        try {
            System.out.println(FileHelper.findClassFileDirectory(TestFileHelper.class));
        } catch (Exception exception) {
        }
        System.out.println(FileHelper.findClassPackageFile(TestFileHelper.class));
        System.out.println(FileHelper.findClassPackageFileDirectory(TestFileHelper.class));
    }

}
