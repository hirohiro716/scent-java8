package com.hirohiro716;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.hirohiro716.file.FileHelper.FileExtension;

/**
 * バイト操作を補助するクラス。
 *
 * @author hiro
 */
public class ByteConverter {

    private String fileLocation;
    private FileExtension fileExtension = FileExtension.UNKNOWN;
    private byte[] bytes;

    /**
     * コンストラクタ。
     *
     */
    public ByteConverter() {
        super();
    }

    /**
     * ファイルを指定してインスタンス生成する。
     *
     * @param fileLocation ファイル名
     * @throws IOException 
     */
    public ByteConverter(String fileLocation) throws IOException {
        this.setFileLocation(fileLocation);
    }

    /**
     * ファイルを指定してインスタンス生成する。
     *
     * @param fileLocation ファイルURI
     * @throws IOException 
     */
    public ByteConverter(File fileLocation) throws IOException {
        this.setFileLocation(fileLocation.getPath());
    }

    /**
     * バイト配列と拡張子を指定してインスタンス生成する。
     *
     * @param bytes バイト配列
     */
    public ByteConverter(byte[] bytes) {
        this.setBytes(bytes, FileExtension.UNKNOWN);
    }

    /**
     * バイト配列と拡張子を指定してインスタンス生成する。
     *
     * @param bytes バイト配列
     * @param fileExtension 拡張子
     */
    public ByteConverter(byte[] bytes, FileExtension fileExtension) {
        this.setBytes(bytes, fileExtension);
    }

    /**
     * ファイルパスを取得する。
     *
     * @return 画像ファイルのフルパス
     */
    public String getFileLocation() {
        return this.fileLocation;
    }

    /**
     * ファイルパスを指定する。
     *
     * @param fileLocation 画像ファイルのフルパス
     * @throws IOException 
     */
    public void setFileLocation(String fileLocation) throws IOException {
        this.fileLocation = fileLocation;
        this.fileExtension = FileExtension.find(fileLocation);
        this.bytes = new byte[1];
        try (FileInputStream inputStream = new FileInputStream(fileLocation)) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while (inputStream.read(this.bytes) > 0) {
                    outputStream.write(this.bytes);
                }
                this.bytes = outputStream.toByteArray();
            }
        }
    }

    /**
     * 画像バイナリを取得する。
     *
     * @return バイト配列
     */
    public byte[] getBytes() {
        return this.bytes;
    }

    /**
     * バイナリをセットする。
     *
     * @param bytes バイト配列
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.fileExtension = FileExtension.UNKNOWN;
    }

    /**
     * バイナリを拡張子指定でセットする。
     *
     * @param bytes バイト配列
     * @param fileExtension 拡張子
     */
    public void setBytes(byte[] bytes, FileExtension fileExtension) {
        this.bytes = bytes;
        this.fileExtension = fileExtension;
    }

    /**
     * ファイル拡張子を取得する。
     *
     * @return 拡張子
     */
    public FileExtension getFileExtension() {
        return this.fileExtension;
    }

    /**
     * byte配列を16進数文字列に変換する。
     *
     * @param bytes 変換対象
     * @return 変化後文字列
     */
    public static String bytesToString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
        // バイト配列の要素数分、処理を繰り返す.
        for (int index = 0; index < bytes.length; index++) {
            // バイト値を自然数に変換.
            int byteInt = bytes[index] & 0xff;
            // バイト値が0x10以下か判定.
            if (byteInt < 0x10) {
                // 0x10以下の場合、文字列バッファに0を追加.
                stringBuilder.append("0");
            }
            // バイト値を16進数の文字列に変換して、文字列バッファに追加.
            stringBuilder.append(Integer.toHexString(byteInt));
        }
        return stringBuilder.toString();
    }

    /**
     * 16進数文字列をbyte配列に変換する。失敗した場合はnullを返す。
     *
     * @param bytesString 変換対象
     * @return 変換後Byte配列
     */
    public static byte[] stringToBytes(String bytesString) {
        if (bytesString == null) {
            return null;
        }
        // 文字列長の1/2の長さのバイト配列を生成.
        byte[] bytes = new byte[bytesString.length() / 2];
        // バイト配列の要素数分、処理を繰り返す.
        for (int index = 0; index < bytes.length; index++) {
            // 16進数文字列をバイトに変換して配列に格納.
            try {
                bytes[index] = (byte) Integer.parseInt(bytesString.substring(index * 2, (index + 1) * 2), 16);
            } catch (NumberFormatException exception) {
                return null;
            }
        }
        return bytes;
    }

    /**
     * 保持しているファイルを指定したファイル名で保存する。
     *
     * @param fileLocation 保存先フルパス
     * @throws IOException 
     */
    public void saveAs(String fileLocation) throws IOException {
        ByteConverter.saveBytesToFile(this.bytes, fileLocation);
    }

    /**
     * byte配列をファイルに保存する。
     *
     * @param bytes 保存対象byte配列
     * @param fileLocation ファイル名
     * @throws java.io.IOException
     */
    public static void saveBytesToFile(byte[] bytes, String fileLocation) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(fileLocation)) {
            stream.write(bytes);
            stream.close();
        }
    }

    /**
     * ファイルを読み込んでbyte配列を取得する。
     *
     * @param file 対象ファイル
     * @return バイナリ
     * @throws java.io.IOException
     */
    public static byte[] loadBytesFromFile(File file) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                return loadBytesFromResource(inputStream);
            }
        }
    }

    /**
     * InputStreamを読み込んでbyte配列を取得する。
     *
     * @param inputStream 対象ストリーム
     * @return バイナリ
     * @throws java.io.IOException
     */
    public static byte[] loadBytesFromResource(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1];
        try (ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream()) {
            while (inputStream.read(bytes) > 0) {
                byteArrayOutStream.write(bytes);
            }
            return byteArrayOutStream.toByteArray();
        }
    }

    /**
     * シリアライズを行いByte配列を取得する。値の部分に入っているオブジェクトのシリアライズはサポートしない。
     *
     * @param serializable シリアライズするオブジェクト
     * @return シリアライズしたByte配列
     * @throws IOException
     */
    public static byte[] serialize(Serializable serializable) throws IOException {
        try (ByteArrayOutputStream bytesOutStream = new ByteArrayOutputStream()) {
            byte[] result;
            try (ObjectOutputStream objOutputStream = new ObjectOutputStream(bytesOutStream)) {
                objOutputStream.writeObject(serializable);
                objOutputStream.reset();
                result = bytesOutStream.toByteArray();
                bytesOutStream.close();
                bytesOutStream.reset();
                return result;
            }
        }
    }
    
    /**
     * Byte配列からデシリアライズを行いインスタンスを復元する。値の部分に入っているオブジェクトのシリアライズはサポートしない。
     *
     * @param <T> インスタンスの型
     * @param serialized byte配列
     * @return デシリアライズしたインスタンス
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T> T desirialize(byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bytesInputStream = new ByteArrayInputStream(serialized);
        try (ObjectInputStream objInputStream = new ObjectInputStream(bytesInputStream)) {
            T object = (T) objInputStream.readObject();
            return object;
        }
    }
}
