package com.hirohiro716.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * JavaMail1.6.1を使用したメール送信を行うクラス.
 * @author hiro
 */
public class EmailTransmitter {

    private InternetAddress[] myMailAddress = new InternetAddress[] {};

    /**
     * 送信元メールアドレスをセットする.
     * @param myMailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void setMyMailAddress(String myMailAddress) throws Exception {
        this.myMailAddress = InternetAddress.parse(myMailAddress);
    }

    private String smtpHost;

    /**
     * 送信メールサーバーを設定.
     * @param smtpHost
     */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    private String smtpUser;

    /**
     * 送信メールサーバー認証ユーザーを設定.
     * @param smtpUser
     */
    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    private String smtpPass;

    /**
     * 送信メールサーバー認証パスワードを設定.
     * @param smtpPass
     */
    public void setSmtpPassword(String smtpPass) {
        this.smtpPass = smtpPass;
    }

    private String smtpPortNumber = "25";

    /**
     * 送信メールポート番号を設定.
     * @param smtpPortNumber
     */
    public void setSmtpPortNumber(int smtpPortNumber) {
        this.smtpPortNumber = String.valueOf(smtpPortNumber);
    }

    private boolean isEnableTls = false;

    /**
     * メールの通信にTLSを使用するかどうか.
     * @param isEnableTls
     */
    public void setEnableTls(boolean isEnableTls) {
        this.isEnableTls = isEnableTls;
    }

    private InternetAddress[] toMailAddress = new InternetAddress[] {};

    /**
     * 送信先(TO)メールアドレスをセットする.
     * @param toMailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void setToMailAddress(String toMailAddress) throws Exception {
        this.toMailAddress = InternetAddress.parse(toMailAddress);
    }

    /**
     * 送信先(TO)メールアドレスを追加する.
     * @param toMailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void addToMailAddress(String toMailAddress) throws Exception {
        ArrayList<InternetAddress> addresses = new ArrayList<>();
        for (InternetAddress address: this.toMailAddress) {
            addresses.add(address);
        }
        for (InternetAddress address: InternetAddress.parse(toMailAddress)) {
            addresses.add(address);
        }
        this.toMailAddress = addresses.toArray(new InternetAddress[] {});
    }

    private InternetAddress[] ccMailAddress = new InternetAddress[] {};

    /**
     * 送信先(CC)メールアドレスをセットする.
     * @param ccMailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void setCcMailAddress(String ccMailAddress) throws Exception {
        this.ccMailAddress = InternetAddress.parse(ccMailAddress);
    }

    /**
     * 送信先(CC)メールアドレスを追加する.
     * @param ccMailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void addCcMailAddress(String ccMailAddress) throws Exception {
        ArrayList<InternetAddress> addresses = new ArrayList<>();
        for (InternetAddress address: this.ccMailAddress) {
            addresses.add(address);
        }
        for (InternetAddress address: InternetAddress.parse(ccMailAddress)) {
            addresses.add(address);
        }
        this.ccMailAddress = addresses.toArray(new InternetAddress[] {});
    }

    private InternetAddress[] bccMailAddress = new InternetAddress[] {};

    /**
     * 送信先(BCC)メールアドレスをセットする.
     * @param bccMailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void setBccMailAddress(String bccMailAddress) throws Exception {
        this.bccMailAddress = InternetAddress.parse(bccMailAddress);
    }

    /**
     * 送信先(BCC)メールアドレスを追加する.
     * @param bccMailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void addBccMailAddress(String bccMailAddress) throws Exception {
        ArrayList<InternetAddress> addresses = new ArrayList<>();
        for (InternetAddress address: this.bccMailAddress) {
            addresses.add(address);
        }
        for (InternetAddress address: InternetAddress.parse(bccMailAddress)) {
            addresses.add(address);
        }
        this.bccMailAddress = addresses.toArray(new InternetAddress[] {});
    }
    
    private String charset = "UTF-8";
    
    /**
     * Charsetをセットする.
     * @param charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }
    
    private boolean isDebug = false;
    
    /**
     * デバッグが有効かどうかをセットする.
     * @param isDebug
     */
    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }
    
    /**
     * メールを送信する.
     * @param subject メール表題
     * @param body 本文
     * @throws Exception メッセージの送信に失敗
     */
    public void send(String subject, String body) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.smtpHost);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", this.smtpPortNumber);
        if (this.isEnableTls) {
            properties.put("mail.smtp.starttls.required", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "true");
        }
        Session session = Session.getInstance(properties, this.new SmtpAuth());
        session.setDebug(this.isDebug);
        MimeMessage mime = new MimeMessage(session);
        if (this.myMailAddress.length > 0) {
            mime.addFrom(this.myMailAddress);
        }
        if (this.toMailAddress.length > 0) {
            mime.addRecipients(Message.RecipientType.TO, this.toMailAddress);
        }
        if (this.ccMailAddress.length > 0) {
            mime.addRecipients(Message.RecipientType.CC, this.ccMailAddress);
        }
        if (this.bccMailAddress.length > 0) {
            mime.addRecipients(Message.RecipientType.BCC, this.bccMailAddress);
        }
        mime.setSubject(subject, this.charset);
        mime.setText(body, this.charset);
        mime.setSentDate(new Date());
        Transport.send(mime);
    }

    private class SmtpAuth extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(EmailTransmitter.this.smtpUser, EmailTransmitter.this.smtpPass);
        }

    }

}
