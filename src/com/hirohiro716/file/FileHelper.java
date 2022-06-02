package com.hirohiro716.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import com.hirohiro716.OSHelper;
import com.hirohiro716.StringConverter;

import static com.hirohiro716.file.FileHelper.FileExtension.*;

/**
 * ファイル操作を補助します。
 *
 * @author hiro
 */
public class FileHelper {

    /**
     * 環境ごとのパス区切り文字を取得しておく
     */
    public static final String FILE_SEPARATOR = File.separator;

    /**
     * ファイルの拡張子
     */
    public enum FileExtension {

        /**
         * JPEGファイル
         */
        JPG(".jpg", ".JPG", ".jpeg", ".JPEG"),

        /**
         * PNGファイル
         */
        PNG(".png", ".PNG"),

        /**
         * GIFファイル
         */
        GIF(".gif", ".GIF"),

        /**
         * ビットマップファイル
         */
        BMP(".bmp", ".BMP"),

        /**
         * テキストファイル
         */
        TXT(".txt", ".TXT"),

        /**
         * CSVファイル
         */
        CSV(".csv", ".CSV"),

        /**
         * XMLファイル
         */
        XML(".xml", ".XML"),

        /**
         * INIファイル
         */
        INI(".ini", ".INI"),

        /**
         * PDFファイル
         */
        PDF(".pdf", ".PDF"),

        /**
         * MS OFFICE Wordファイル
         */
        DOC(".doc", ".DOC"),

        /**
         * MS OFFICE Excelファイル
         */
        XLS(".xls", ".XLS"),

        /**
         * ZIPアーカイブ
         */
        ZIP(".zip", ".ZIP"),

        /**
         * LZHアーカイブ
         */
        LZH(".lzh", ".LZH"),

        /**
         * HTMLドキュメント
         */
        HTML(".html", ".HTML", ".htm", ".HTM"),

        /**
         * CSSスタイルシート
         */
        CSS(".css", ".CSS"),

        /**
         * 音楽ファイル
         */
        MP3(".mp3", ".MP3"),

        /**
         * 音楽ファイル
         */
        WAV(".wav", ".WAV"),

        /**
         * 音楽ファイル
         */
        M4A(".m4a", ".M4A"),

        /**
         * 音楽ファイル
         */
        WMA(".wma", ".WMA"),

        /**
         * 動画ファイル
         */
        WMV(".wmv", ".WMV"),

        /**
         * 動画ファイル
         */
        AVI(".avi", ".AVI"),

        /**
         * 動画ファイル
         */
        MPG(".mpg", ".MPG", ".mpeg", ".MPEG"),

        /**
         * 動画ファイル
         */
        MP4(".mp4", ".MP4"),

        /**
         * DATファイル
         */
        DAT(".dat", ".DAT"),

        /**
         * 不明
         */
        UNKNOWN("."), ;
        private final String[] extensions;

        /**
         * 拡張子文字列
         */
        public final String text;

        private FileExtension(String... extensions) {
            this.extensions = extensions;
            this.text = this.toString().toLowerCase();
        }

        /**
         * ファイル名から一致する拡張子を取得する。
         *
         * @param fileName ファイル名
         * @return 拡張子
         */
        public static FileExtension find(String fileName) {
            if (fileName == null) {
                return FileExtension.UNKNOWN;
            }
            for (FileExtension fileExtension : values()) {
                for (String s : fileExtension.extensions) {
                    if (s.endsWith(fileName) || fileName.endsWith(s)) {
                        return fileExtension;
                    }
                }
            }
            return FileExtension.UNKNOWN;
        }
    }

    /**
     * ファイルの拡張子タイプ
     */
    public enum FileExtensionType {
        /**
         * 画像ファイル
         */
        IMAGE(BMP, JPG, PNG, GIF),
        /**
         * テキストファイル
         */
        TEXT(TXT, CSV, XML, INI, HTML, CSS),
        /**
         * サウンドファイル
         */
        SOUND(MP3, WMA, M4A, WAV),
        /**
         * 動画ファイル
         */
        MOVIE(WMV, AVI, MPG, MP4),
        /**
         * 圧縮ファイル
         */
        ARCHIVE(ZIP, LZH),
        /**
         * その他ファイル
         */
        OTHER(UNKNOWN),
        ;
        private final FileExtension[] extensions;
        private FileExtensionType(FileExtension... fileExtension) {
            this.extensions = fileExtension;
        }

        /**
         * ファイル拡張子から種類を取得する。
         *
         * @param fileExtension 拡張子
         * @return 種類
         */
        public static FileExtensionType find(FileExtension fileExtension) {
            for (FileExtensionType type : FileExtensionType.values()) {
                for (FileExtension extension : type.extensions) {
                    if (fileExtension != null && extension == fileExtension) {
                        return type;
                    }
                }
            }
            return FileExtensionType.OTHER;
        }
    }

    /**
     * ファイルの所属するRootを取得する。
     *
     * @param file 対象ファイル
     * @return Root
     */
    public static File findFileRoot(File file) {
        File[] files = File.listRoots();
        for (int i = 0; i < files.length; i++) {
            if (file.toPath().startsWith(files[i].toPath())) {
                return files[i];
            }
        }
        return null;
    }
    
    /**
     * ディレクトリ内のファイルをサブディレクトリを含めすべて取得する。
     *
     * @param directory 対象ディレクトリ
     * @param filenameFilter フィルタ(nullも可)
     * @return 見つかったファイル
     */
    public static File[] findAllFilesFromDirectory(File directory, FilenameFilter filenameFilter) {
        ArrayList<File> files = new ArrayList<>();
        if (directory != null && directory.isDirectory()) {
            for (File file: directory.listFiles()) {
                if (filenameFilter ==null || filenameFilter.accept(file, file.getName())) {
                    files.add(file);
                }
                if (file.isDirectory()) {
                    for (File fileOfSubDirectory: findAllFilesFromDirectory(file, filenameFilter)) {
                        files.add(fileOfSubDirectory);
                    }
                }
            }
        }
        return files.toArray(new File[] {});
    }

    /**
     * フォルダを新規作成する。
     *
     * @param location 作成フォルダ
     * @return 結果
     */
    public static boolean makeDirectory(String location) {
        File file = new File(location);
        if (file.isDirectory() == false) {
            if (file.mkdirs() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * フォルダの存在を確認する。
     *
     * @param location 確認フォルダ
     * @return 結果
     */
    public static boolean isExistsDirectory(String location) {
        File setDir = new File(location);
        return setDir.isDirectory();
    }

    /**
     * ファイルの存在を確認する。
     *
     * @param location 確認ファイル
     * @return 結果
     */
    public static boolean isExistsFile(String location) {
        File setFile = new File(location);
        return setFile.isFile();
    }

    /**
     * URIを実行環境OS用に最適化されたパスに変換する。
     *
     * @param location 対象URI
     * @return 実行環境OS用パス
     */
    public static String generateOptimizedPathFromURI(URI location) {
        String tempPath = location.getPath();
        switch (OSHelper.findOS()) {
        case WINDOWS:
            if (location.getHost() != null) {
                StringBuilder builder = new StringBuilder("//");
                builder.append(location.getHost());
                builder.append(tempPath);
                tempPath = builder.toString();
            } else {
                if (tempPath.indexOf("//") < 0) {
                    tempPath = tempPath.substring(1);
                }
            }
            break;
        case LINUX:
        case MAC:
        case UNKNOWN:
            break;
        }
        tempPath = tempPath.replace("/", File.separator);
        return tempPath;
    }

    /**
     * ファイルの親フォルダを取得する. 引数がフォルダの場合はそのまま返す。
     *
     * @param location ファイル名
     * @return フォルダのFileオブジェクト
     */
    public static URI findDirectoryFromFile(URI location) {
        if (location.getScheme().equals("jar")) {
            return null;
        }
        File setFile = new File(location);
        if (setFile.isDirectory()) {
            return setFile.toURI();
        }
        if (setFile.isFile()) {
            return setFile.getParentFile().toURI();
        }
        return null;
    }

    /**
     * クラスのフルパスを取得する。
     *
     * @param c 取得するクラス
     * @return フルパス
     */
    public static URI findClassFile(Class<?> c) {
        try {
            String classFileName = "/" + c.getName().replaceAll(File.separator, "/") + ".class";
            return c.getResource(classFileName).toURI();
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    /**
     * クラスの親フォルダを取得する。
     *
     * @param c 取得するクラス
     * @return 親フォルダ絶対パス
     */
    public static URI findClassFileDirectory(Class<?> c) {
        URI dir = findDirectoryFromFile(findClassFile(c));
        return dir;
    }

    /**
     * クラスのパッケージファイル名を取得する。
     *
     * @param c 取得するクラス
     * @return パッケージファイル名
     */
    public static URI findClassPackageFile(Class<?> c) {
        ProtectionDomain domain = c.getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        if (source != null) {
            try {
                return source.getLocation().toURI();
            } catch (URISyntaxException ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * パッケージの親フォルダを取得する。
     *
     * @param c 取得するクラス
     * @return 親フォルダ絶対パス
     */
    public static URI findClassPackageFileDirectory(Class<?> c) {
        URI dir = findDirectoryFromFile(findClassPackageFile(c));
        return dir;
    }

    /**
     * ファイルをコピーする。
     *
     * @param orgFile コピー元ファイル
     * @param copyFile コピー先ファイル
     * @param bufferByteSize バッファ
     * @throws IOException
     */
    public static void copyFile(File orgFile, File copyFile, int bufferByteSize) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(orgFile); FileOutputStream outputStream = new FileOutputStream(copyFile)) {
            byte[] buffer = new byte[bufferByteSize];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        }
    }

    /**
     * ファイルをコピーします。
     *
     * @param orgInputStream コピー元ファイルストリーム
     * @param copyFile コピー先ファイル
     * @param bufferByteSize バッファ
     * @throws IOException
     */
    public static void copyFile(InputStream orgInputStream, File copyFile, int bufferByteSize) throws IOException {
        try (InputStream inputStream = orgInputStream; FileOutputStream outputStream = new FileOutputStream(copyFile)) {
            byte buffer[] = new byte[bufferByteSize];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
        }
    }
    
    /**
     * テキストファイルを作成する。
     *
     * @param contents 内容
     * @param file ファイルオブジェクト
     * @param charset 文字セット
     * @throws IOException
     */
    public static void createTextFile(String contents, File file, Charset charset) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), charset)) {
            writer.write(contents);
            writer.flush();
        }
    }

    /**
     * テキストファイルをデフォルトの文字セットを使用して作成する。
     *
     * @param contents 内容
     * @param file ファイルオブジェクト
     * @throws IOException
     */
    public static void createTextFile(String contents, File file) throws IOException {
        createTextFile(contents, file, Charset.defaultCharset());
    }
    
    /**
     * テキストファイルに内容を読み込む。
     *
     * @param file ファイルオブジェクト
     * @param charset 文字セット
     * @return ファイルの内容
     * @throws IOException
     */
    public static String readTextFile(File file, Charset charset) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            try (InputStreamReader streamReader = new InputStreamReader(stream, charset)) {
                try (BufferedReader bufferedReader = new BufferedReader(streamReader)) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (result.length() > 0) {
                            result.append(StringConverter.LINE_SEPARATOR);
                        }
                        result.append(line);
                    }
                    return result.toString();
                }
            }
        }
    }
    
    /**
     * テキストファイルの内容をデフォルトの文字セットを使用して読み込む。
     *
     * @param file ファイルオブジェクト
     * @return ファイルの内容
     * @throws IOException
     */
    public static String readTextFile(File file) throws IOException {
        return readTextFile(file, Charset.defaultCharset());
    }
}
