package com.hirohiro716.awt;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * java.awtでQRコードを生成するクラス.
 * 
 * @author hiro
 */
public class QRCodeHelper {
    
    /**
     * QRコードのBufferedImageオブジェクトを作成する.
     * @param contents 内容
     * @param width 幅ピクセル
     * @param height 高さピクセル
     * @return BufferedImage
     * @throws Exception
     */
    public static BufferedImage createBufferedImage(String contents, int width, int height) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    /**
     * QRコードのJPEG画像を保存する.
     * @param contents 内容
     * @param width 幅ピクセル
     * @param height 高さピクセル
     * @param file JPEGファイル
     * @throws Exception 
     */
    public static void writeJpegFile(String contents, int width, int height, File file) throws Exception {
        ImageIO.write(createBufferedImage(contents, width, height), "jpg", file);
    }
    
}
