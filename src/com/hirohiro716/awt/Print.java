package com.hirohiro716.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Locale;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;

import com.hirohiro716.file.FileHelper.FileExtension;
import com.hirohiro716.print.PrintHelper;

/**
 * 印刷の補助をするクラス
 * @author hiro
 */
public class Print {

    // 印刷物指定
    private Printable instancePrintable;
    // 属性セットを保持
    private final PrintRequestAttributeSet instancePrintRequestAttributeSet = new HashPrintRequestAttributeSet();
    // プリンタジョブのインスタンス
    private final PrinterJob instancePrintJob = PrinterJob.getPrinterJob();
    // 用紙サイズ保持
    private float instanceMediaSizeWidth;
    private float instanceMediaSizeHeight;
    private MediaSize instanceMediaSize;
    private OrientationRequested instancePaperLandscape;
    // 用紙トレイ保持
    private String instanceMediaTray;

    /**
     * プリンタ一覧を取得します。
     *
     * @return PrintService配列
     */
    public static PrintService[] fetchPrintServices() {
        return PrinterJob.lookupPrintServices();
    }

    /**
     * 印刷物を追加します。
     *
     * @param printable Printableインターフェースを継承したクラスインスタンス
     */
    public void setPrintable(Printable printable) {
        this.instancePrintable = printable;
    }

    /**
     * 印刷するプリンタを設定します。
     *
     * @param printService 対象PrintService
     * @throws PrinterException 指定されたサービスが有効な印刷サービスではない場合
     */
    public void setPrintingService(PrintService printService) throws PrinterException {
        this.instancePrintJob.setPrintService(printService);
    }

    /**
     * 用紙向きを指定します。
     *
     * @param orientationRequested 向き定数
     */
    public void setPaperOrientation(OrientationRequested orientationRequested) {
        this.instancePrintRequestAttributeSet.add(orientationRequested);
        this.instancePaperLandscape = orientationRequested;
    }

    /**
     * プリンタのトレイを指定します。
     *
     * @param trayName
     */
    public void setMediaTray(String trayName) {
        this.instanceMediaTray = trayName;
        this.setMediaTrayAttribute();
    }

    /**
     * トレイを実際にセットします。
     *
     */
    private void setMediaTrayAttribute() {
        if (this.instanceMediaTray.length() > 0) {
            Object values = this.instancePrintJob.getPrintService().getSupportedAttributeValues(Media.class, null, null);
            if (values instanceof Media[]) {
                Media[] media = (Media[]) values;
                for (Media m : media) {
                    if (m instanceof MediaTray) {
                        if (this.instanceMediaTray.equals(m.toString())) {
                            this.instancePrintRequestAttributeSet.add(m);
                        }
                    }
                }
            }
        }
    }

    /**
     * 用紙サイズをミリメートルで指定します。
     *
     * @param width 幅(mm)
     * @param height 高さ(mm)
     */
    public void setPaperSize(float width, float height) {
        MediaSizeName sizeName = MediaSize.findMedia(width, height, Size2DSyntax.MM);
        if (sizeName != null
                && MediaSize.getMediaSizeForName(sizeName).getX(Size2DSyntax.MM) == width
                && MediaSize.getMediaSizeForName(sizeName).getY(Size2DSyntax.MM) == height) {
            this.instanceMediaSizeWidth = 0;
            this.instanceMediaSizeHeight = 0;
            this.instanceMediaSize = MediaSize.getMediaSizeForName(sizeName);
            this.instancePrintRequestAttributeSet.add(sizeName);
        } else {
            this.instanceMediaSizeWidth = width;
            this.instanceMediaSizeHeight = height;
        }
    }

    /**
     * 用紙サイズをPaperSize列挙型から選択して指定します。
     *
     * @param sizeName
     */
    public void setPaperSize(MediaSizeName sizeName) {
        this.instanceMediaSizeWidth = 0;
        this.instanceMediaSizeHeight = 0;
        this.instanceMediaSize = MediaSize.getMediaSizeForName(sizeName);
        this.instancePrintRequestAttributeSet.add(sizeName);
    }

    /**
     * 用紙に印刷させる範囲を設定します。
     *
     * @param x 左開始位置
     * @param y 上開始位置
     * @param width 幅
     * @param height 高さ
     */
    public void setPaperPrintArea(float x, float y, float width, float height) {
        MediaPrintableArea area = new MediaPrintableArea(x, y, width, height, MediaPrintableArea.MM);
        this.instancePrintRequestAttributeSet.add(area);
    }

    /**
     * 印刷ジョブ名を指定します。
     *
     * @param jobName ジョブ名
     */
    public void setJobName(String jobName) {
        JobName jName = new JobName(jobName, Locale.getDefault());
        this.instancePrintRequestAttributeSet.add(jName);
    }

    /**
     * 印刷部数を指定します。
     *
     * @param copies コピー回数
     */
    public void setCopies(int copies) {
        Copies copiesInstance = new Copies(copies);
        this.instancePrintRequestAttributeSet.add(copiesInstance);
    }

    /**
     * カラーかモノクロを指定します。
     *
     * @param isColor カラーかどうか
     */
    public void setColor(boolean isColor) {
        if (isColor) {
            this.instancePrintRequestAttributeSet.add(Chromaticity.COLOR);
        } else {
            this.instancePrintRequestAttributeSet.add(Chromaticity.MONOCHROME);
        }
    }

    /**
     * 印刷ダイアログを表示して印刷設定をします。
     *
     * @return 結果
     */
    public boolean showPrintDialog() {
        return this.instancePrintJob.printDialog(this.instancePrintRequestAttributeSet);
    }

    /**
     * 印刷を実行します。
     *
     * @throws PrinterException 印刷システムのエラーが原因でジョブが停止した場合
     */
    public void print() throws PrinterException {
        this.setMediaTrayAttribute();
        this.instancePrintJob.setPrintable(this.instancePrintable);
        this.instancePrintJob.print(this.instancePrintRequestAttributeSet);
    }

    /**
     * 印刷結果の画像データを保持したImageHelperインスタンスを取得します。
     *
     * @param zoomRate 拡大比率(1が等倍・0.5で半分)
     * @param fileExtension 画像タイプ
     * @return ImageHelperインスタンス
     * @throws PrinterException
     * @throws IOException
     */
    public ImageConverter printToImageConverter(float zoomRate, FileExtension fileExtension) throws PrinterException, IOException {
        float sizeWidth;
        float sizeHeight;
        if (this.instanceMediaSizeHeight == 0 && this.instanceMediaSizeWidth == 0) {
            if (this.instanceMediaSize == null) {
                throw new PrinterException("用紙サイズが設定されていません。");
            }
            // サイズを取得して画像サイズを決定する
            sizeWidth = this.instanceMediaSize.getX(Size2DSyntax.MM) * zoomRate;
            sizeHeight = this.instanceMediaSize.getY(Size2DSyntax.MM) * zoomRate;
        } else {
            // サイズを取得して画像サイズを決定する
            sizeWidth = this.instanceMediaSizeWidth * zoomRate;
            sizeHeight = this.instanceMediaSizeHeight * zoomRate;
        }
        // 横向きなら画像サイズをチェンジ
        if (this.instancePaperLandscape == OrientationRequested.LANDSCAPE
                || this.instancePaperLandscape == OrientationRequested.REVERSE_LANDSCAPE) {
            float tempw = sizeWidth;
            sizeWidth = sizeHeight;
            sizeHeight = tempw;
        }
        // 新規で画像を作成
        BufferedImage bufferedImage = new BufferedImage((int) PrintHelper.millimeterToPoint(sizeWidth), (int) PrintHelper.millimeterToPoint(sizeHeight), BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = bufferedImage.createGraphics();
        // 画質調整
        g2d.transform(AffineTransform.getScaleInstance(zoomRate, zoomRate));
        // アンチエイリアス処理
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 背景を白で描画
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, (int) PrintHelper.millimeterToPoint(sizeWidth), (int) PrintHelper.millimeterToPoint(sizeHeight));
        // 描画色を黒に戻して印字処理
        g2d.setColor(Color.black);
        this.instancePrintable.print(g2d, null, 0);
        // ImageHelperにセットして返す
        ImageConverter imageHelper = new ImageConverter();
        imageHelper.applyBufferedImage(bufferedImage, fileExtension);
        return imageHelper;
    }
}
