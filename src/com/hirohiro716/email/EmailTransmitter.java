package com.hirohiro716.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * JavaMail1.6.1を使用したE-mail送信を行うクラス.
 * @author hiro
 */
public class EmailTransmitter {

    private InternetAddress[] myEmailAddress = new InternetAddress[] {};

    /**
     * 送信元メールアドレスをセットする.
     * @param emailAddress
     * @throws Exception アドレスの有効性確認失敗
     */
    public void setMyEmailAddress(String emailAddress) throws Exception {
        this.myEmailAddress = InternetAddress.parse(emailAddress);
    }

    private String host;

    /**
     * 送信メールサーバーを設定.
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    private String user;

    /**
     * 送信メールサーバー認証ユーザーを設定.
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    private String password;

    /**
     * 送信メールサーバー認証パスワードを設定.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    private String portNumber = "25";

    /**
     * 送信メールポート番号を設定.
     * @param portNumber
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = String.valueOf(portNumber);
    }

    private boolean isEnableTLS = false;

    /**
     * メールの通信にTLSを使用するかどうか.
     * @param isEnableTLS
     */
    public void setEnableTLS(boolean isEnableTLS) {
        this.isEnableTLS = isEnableTLS;
    }
    
    private HashMap<ReceiverType, ArrayList<InternetAddress>> receiverEmailAddresses = new HashMap<>();
    
    /**
     * 受信者のE-mailアドレスを追加する.
     * @param emailAddress E-mailアドレス
     * @param receiverType 受信者タイプ
     * @throws Exception
     */
    public void addReceiverEmailAddress(String emailAddress, ReceiverType receiverType) throws Exception {
        if (this.receiverEmailAddresses.containsKey(receiverType) == false) {
            this.receiverEmailAddresses.put(receiverType, new ArrayList<>());
        }
        ArrayList<InternetAddress> emailAddresses = this.receiverEmailAddresses.get(receiverType);
        emailAddresses.add(InternetAddress.parse(emailAddress)[0]);
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
        properties.put("mail.smtp.host", this.host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", this.portNumber);
        if (this.isEnableTLS) {
            properties.put("mail.smtp.starttls.required", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "true");
        }
        Session session = Session.getInstance(properties, this.new SmtpAuth());
        session.setDebug(this.isDebug);
        MimeMessage mime = new MimeMessage(session);
        if (this.myEmailAddress.length > 0) {
            mime.addFrom(this.myEmailAddress);
        }
        ArrayList<InternetAddress> emailAddresses = this.receiverEmailAddresses.get(ReceiverType.TO);
        if (emailAddresses != null && emailAddresses.size() > 0) {
            mime.addRecipients(Message.RecipientType.TO, emailAddresses.toArray(new InternetAddress[] {}));
        }
        emailAddresses = this.receiverEmailAddresses.get(ReceiverType.CC);
        if (emailAddresses != null && emailAddresses.size() > 0) {
            mime.addRecipients(Message.RecipientType.CC, emailAddresses.toArray(new InternetAddress[] {}));
        }
        emailAddresses = this.receiverEmailAddresses.get(ReceiverType.BCC);
        if (emailAddresses != null && emailAddresses.size() > 0) {
            mime.addRecipients(Message.RecipientType.BCC, emailAddresses.toArray(new InternetAddress[] {}));
        }
        mime.setSubject(subject, this.charset);
        mime.setText(body, this.charset);
        mime.setSentDate(new Date());
        Transport.send(mime);
    }

    private class SmtpAuth extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(EmailTransmitter.this.user, EmailTransmitter.this.password);
        }

    }
    
    /**
     * 受信者の種類列挙型.
     * @author hiro
     */
    public enum ReceiverType {
        /**
         * 宛先
         */
        TO,
        /**
         * CC
         */
        CC,
        /**
         * BCC
         */
        BCC,
    }
    
}
