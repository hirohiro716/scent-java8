package test;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.hirohiro716.file.xml.XML;


@SuppressWarnings("all")
public class TestXmlHelper {

    public static void main(String[] args) {
        XML helper = new XML();
        try {
//            Node node = helper.createNode("test");
//            node.setTextContent("aaaa");
//            helper.saveFile("C:\\Users\\hiro\\Desktop\\test.xml", "UTF-8");

            helper.importFromSourceString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test>aaaa</test>");
            System.out.println(helper.findNodeByName("test").getTextContent());

            helper.importFromFile("C:\\Users\\hiro\\Desktop\\test.xml");
            System.out.println(helper.findNodeByName("test").getTextContent());

        } catch (IOException | SAXException exception) {
            exception.printStackTrace();
        }
    }

}
