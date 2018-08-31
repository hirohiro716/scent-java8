package test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.hirohiro716.ByteConverter;
import com.hirohiro716.awt.QRCodeHelper;

@SuppressWarnings("all")
public class TestQRCodeHelper {
    
    public static void main(String[] args) {
        
        try {
            QRCodeHelper.writeJpegFile("https://knowhow.hirohiro716.com/", 200, 200, new File("/home/hiro/Desktop/aaaaa.jpg"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }

}
