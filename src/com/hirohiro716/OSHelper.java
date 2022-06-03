package com.hirohiro716;

/**
 * OSに関する処理をするクラス。
 *
 * @author hiro
 *
 */
public class OSHelper {

    @SuppressWarnings("javadoc")
    public enum OS {
        WINDOWS,
        LINUX,
        MAC,
        UNKNOWN
    }

    /**
     * OSを取得する。
     *
     * @return OS
     */
    public static OS findOS() {
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
            return OS.WINDOWS;
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0) {
            return OS.LINUX;
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
            return OS.MAC;
        }
        return OS.UNKNOWN;
    }
}
