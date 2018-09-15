package com.hirohiro716.awt;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.hirohiro716.ByteConverter;
import com.hirohiro716.file.FileHelper.FileExtension;

/**
 * 画像の処理を補助するクラスです.
 * @author hiro
 */
public class ImageConverter extends ByteConverter {

    /**
     * コンストラクタ.
     */
    public ImageConverter() {
    }

    /**
     * コンストラクタ.
     * @param image
     * @param fileExtension
     */
    public ImageConverter(byte[] image, FileExtension fileExtension) {
        super(image, fileExtension);
    }

    /**
     * コンストラクタ.
     * @param file
     */
    public ImageConverter(File file) {
        super(file);
    }

    /**
     * コンストラクタ.
     * @param fileLocation
     */
    public ImageConverter(String fileLocation) {
        super(fileLocation);
    }

    /**
     * BufferedImageインスタンスをセットします.
     * @param bufferedImage BufferedImageインスタンス
     * @param fileExtension 拡張子
     * @throws IOException 書込み中にエラーが発生した場合
     */
    public void applyBufferedImage(BufferedImage bufferedImage, FileExtension fileExtension) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutStream = new BufferedOutputStream(outStream);
        bufferedImage.flush();
        ImageIO.write(bufferedImage, fileExtension.text, bufferedOutStream);
        this.setBytes(outStream.toByteArray(), fileExtension);
    }

    /**
     * BufferedImageインスタンスをセットします.
     * @param bufferedImage BufferedImageインスタンス
     * @throws IOException 書込み中にエラーが発生した場合
     */
    public void applyBufferedImage(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutStream = new BufferedOutputStream(outStream);
        bufferedImage.flush();
        ImageIO.write(bufferedImage, FileExtension.JPG.text, bufferedOutStream);
        this.setBytes(outStream.toByteArray(), FileExtension.JPG);
    }

    /**
     * BufferedImageインスタンスを取得します.
     * @return インスタンス
     * @throws IOException 読込み中にエラーが発生した場合
     */
    public BufferedImage createBufferedImage() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(this.getBytes());
        BufferedImage image;
        image = ImageIO.read(inputStream);
        return image;
    }

    /**
     * 画像ファイルを指定されたサイズにリサイズします.
     * @param width 幅
     * @param height 高さ
     * @param isExpansion 拡大リサイズをするかどうか
     * @throws IOException 読込み中または書き込み中にエラーが発生した場合
     */
    public void resize(int width, int height, boolean isExpansion) throws IOException {
        if (this.getBytes() == null) {
            return;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(this.getBytes());
        BufferedImage image;
        image = ImageIO.read(inputStream);
        if (isExpansion == false && image.getWidth() <= width && image.getHeight() <= height) {
            return;
        }
        BufferedImage resized = new BufferedImage(width, height, image.getType());
        resized.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
        this.applyBufferedImage(resized);
    }

    /**
     * 画像ファイルを指定されたサイズにリサイズします.
     * @param longSide 高さまたは幅の長辺
     * @param isExpansion 拡大リサイズをするかどうか
     * @return 結果
     * @throws IOException 読込み中または書き込み中にエラーが発生した場合
     */
    public boolean resize(int longSide, boolean isExpansion) throws IOException {
        if (this.getBytes() == null) {
            return false;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(this.getBytes());
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException exception) {
            return false;
        }
        // 比率の計算
        boolean widthLongFlag = false;
        if (image.getWidth() > image.getHeight()) {
            widthLongFlag = true;
        }
        float rate;
        int width;
        int height;
        if (widthLongFlag) {
            rate = (Float.valueOf(image.getWidth()) / Float.valueOf(image.getHeight()));
            width = longSide;
            height = Math.round(longSide / rate);
        } else {
            rate = (Float.valueOf(image.getHeight()) / Float.valueOf(image.getWidth()));
            height = longSide;
            width = Math.round(longSide / rate);
        }
        this.resize(width, height, isExpansion);
        return true;
    }

}
