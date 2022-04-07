package com.kejian.one.net;

import com.kejian.one.App;
import com.kejian.one.utils.CLogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author Zhenxi on 2020-08-31
 */
public class Verifier {

    //本地证书流,实现本地证书的加载
    private  static  InputStream certificate;

    static {
        try {
            certificate = App.getAppContext().getAssets().open("NetLyhKey.cer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //CertificateFactory 用来证书生成
    private  static CertificateFactory certificateFactory;

    static {
        try {
            certificateFactory = CertificateFactory.getInstance("X509");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }



















    public static SSLSocketFactory getSSLFactory(){

        try {


            //创建一个包含可信ca的密钥存储库
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);


            try {
                //读取本地证书，创建一个包含可信ca的密钥存储库
                keyStore.setCertificateEntry(Integer.toString(1), certificateFactory.generateCertificate(certificate));
                certificate.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //创建一个信任管理器，它信任密钥存储库中的ca
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //创建一个使用我们的TrustManager的SSLContext
            SSLContext  sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            //返回true是接受任何域名服务器
            CLogUtils.e("verify 参数 1 HostName "+hostname);
            long creationTime = session.getCreationTime();
            CLogUtils.e("verify 参数 2 getCreationTime "+creationTime);
            String peerHost = session.getPeerHost();
            CLogUtils.e("verify 参数 2 peerHost "+peerHost);
            return true;
        }
    }

}
