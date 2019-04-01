package com.hirohiro716.file.xml;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XMLファイルの作成をサポートします.
 * @author hiro
 */
public class XML {

    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document document;

    /**
     * コンストラクタでorg.w3c.dom.Documentインスタンスを生成する.
     */
    public XML() {
        try {
            this.factory = DocumentBuilderFactory.newInstance();
            this.builder = this.factory.newDocumentBuilder();
            this.document = this.builder.newDocument();
        } catch (ParserConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * org.w3c.dom.Documentインスタンスを取得する.
     * @return Document
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * 新しい要素を作成する.
     * @param name 要素名
     * @return 作成した要素
     */
    public Node createNode(String name) {
        Node node = this.document.createElement(name);
        this.document.appendChild(node);
        return node;
    }

    /**
     * 新しい要素を作成する.
     * @param name 要素名
     * @param parentNode 親要素
     * @return 作成した要素
     */
    public Node createNode(String name, Node parentNode) {
        Node node = this.document.createElement(name);
        parentNode.appendChild(node);
        return node;
    }

    /**
     * 新しい要素を作成して値をセットする.
     * @param name 要素名
     * @param textContent 値
     * @return 作成した要素
     */
    public Node createNode(String name, String textContent) {
        Node node = this.document.createElement(name);
        node.setTextContent(textContent);
        this.document.appendChild(node);
        return node;
    }

    /**
     * 新しい要素を作成して値をセットする.
     * @param name 要素名
     * @param parentNode 親要素
     * @param textContent 値
     * @return 作成した要素
     */
    public Node createNode(String name, Node parentNode, String textContent) {
        Node node = this.document.createElement(name);
        node.setTextContent(textContent);
        parentNode.appendChild(node);
        return node;
    }

    /**
     * 要素をID属性で検索する. 検索結果がない場合はnullを返す.
     * @param id
     * @return 要素
     */
    public Node findNodeById(String id) {
        return this.document.getElementById(id);
    }

    /**
     * 要素を名前で検索する.
     * @param name 要素名
     * @return 要素
     */
    public Node findNodeByName(String name) {
        NodeList nodeList = this.document.getElementsByTagName(name);
        if (nodeList.getLength() == 0) {
            return null;
        }
        return nodeList.item(0);
    }

    /**
     * 要素を名前で検索する.
     * @param name 要素名
     * @return 要素リスト
     */
    public ArrayList<Node> findNodesByName(String name) {
        ArrayList<Node> nodes = new ArrayList<>();
        NodeList nodeList = this.document.getElementsByTagName(name);
        for (int index = 0; index < nodeList.getLength(); index++) {
            nodes.add(nodeList.item(index));
        }
        return nodes;
    }

    /**
     * 要素を名前で検索する.
     * @param name 要素名
     * @param parentNode 親要素
     * @return 要素
     */
    public static Node findNodeByName(String name, Node parentNode) {
        NodeList nodeList = parentNode.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            if (nodeList.item(index).getNodeName().equals(name)) {
                return nodeList.item(index);
            }
        }
        return null;
    }

    /**
     * 要素を名前で検索する.
     * @param name 要素名
     * @param parentNode 親要素
     * @return 要素リスト
     */
    public static ArrayList<Node> findNodesByName(String name, Node parentNode) {
        ArrayList<Node> nodes = new ArrayList<>();
        NodeList nodeList = parentNode.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            if (nodeList.item(index).getNodeName().equals(name)) {
                nodes.add(nodeList.item(index));
            }
        }
        return nodes;
    }

    /**
     * XMLの内容から文字列を生成する.
     * @return XML
     * @throws TransformerException
     */
    public String buildSource() throws TransformerException {
        DOMSource domSource = new DOMSource(this.document);
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(domSource, streamResult);
        return stringWriter.toString();
    }

    /**
     * XMLの内容から文字列を生成する.
     * @param encoding 文字コード（UTF-8/Shift-JISなど）
     * @return XML
     * @throws TransformerException
     */
    public String buildSource(String encoding) throws TransformerException {
        DOMSource domSource = new DOMSource(this.document);
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
        transformer.transform(domSource, streamResult);
        return stringWriter.toString();
    }

    /**
     * XMLファイルを作成する.
     * @param fileLocation 保存先
     * @param encoding 文字コード（UTF-8/Shift-JISなど）
     * @throws IOException
     * @throws TransformerException
     */
    public void saveFile(String fileLocation, String encoding) throws IOException, TransformerException {
        try (OutputStream outputStream = new FileOutputStream(fileLocation)) {
            DOMSource domSource = new DOMSource(this.document);
            StreamResult streamResult = new StreamResult(outputStream);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            transformer.transform(domSource, streamResult);
        }
    }

    /**
     * XMLの内容を取り込む.
     * @param source XML内容
     * @throws SAXException
     */
    public void importFromSourceString(String source) throws SAXException {
        String encoding = "UTF-8";
        int encodingIndex = source.indexOf("encoding=");
        if (encodingIndex > -1) {
            String encodingString = source.substring(encodingIndex + 10);
            encoding = encodingString.substring(0, encodingString.indexOf("\""));
        }
        try {
            this.document = this.builder.parse(new ByteArrayInputStream(source.getBytes(encoding)));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * XMLの内容を取り込む.
     * @param source XML内容
     * @param encoding 文字コード（UTF-8/Shift-JISなど）
     * @throws IOException
     * @throws SAXException
     */
    public void importFromSourceString(String source, String encoding) throws IOException, SAXException {
        this.document = this.builder.parse(new ByteArrayInputStream(source.getBytes(encoding)));
    }

    /**
     * XMLファイルを作成する.
     * @param fileLocation 保存先
     * @throws IOException
     * @throws SAXException
     */
    public void importFromFile(String fileLocation) throws IOException, SAXException {
        this.document = this.builder.parse(fileLocation);
    }

}
