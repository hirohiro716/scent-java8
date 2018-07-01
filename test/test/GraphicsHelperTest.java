package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hirohiro716.ByteConverter;
import static com.hirohiro716.awt.GraphicsHelper.*;
import com.hirohiro716.awt.ImageConverter;
import com.hirohiro716.file.FileHelper.FileExtension;


@SuppressWarnings("all")
public class GraphicsHelperTest {

    public static void main(String[] args) throws IOException {
        ByteConverter byteHelper = new ByteConverter();

        ImageConverter imageHelper = new ImageConverter();

        BufferedImage image = new BufferedImage(2000, 1600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 2000, 1600);

        g2d.setColor(Color.BLACK);
        drawStringBottomRight("test", 0, 1600, 2000, new Font("游ゴシック", Font.PLAIN, 40), g2d);

        g2d.drawRect(10, 10, 200, 200);
        drawStringCenterAutoMultiLine("abcdefghijklmn", 10, 10, 200, 200, new Font("遊明朝", Font.PLAIN, 40), g2d);

        g2d.drawRect(410, 410, 200, 200);
        drawStringAutoMultiLine("abcdefghijklmn", 410, 410, 200, 200, new Font("遊明朝", Font.PLAIN, 40), g2d);

        imageHelper.applyBufferedImage(image, FileExtension.JPG);
        imageHelper.resize(1000, false);
        imageHelper.saveAs("C:\\Users\\hiro\\Desktop\\test.jpg");

    }

}
