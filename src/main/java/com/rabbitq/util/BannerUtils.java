package com.rabbitq.util;

import cn.hutool.core.net.SSLUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpConnection;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rabbitq.Entity.Banner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BannerUtils {
    public static String getHash(String body) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        return md5.digestHex(body);
    }
    public static String getICP(String body) {
        String provincesString = "京|津|沪|渝|冀|晋|辽|吉|黑|苏|浙|皖|闽|赣|蜀|黔|滇|秦|陇|鲁|豫|鄂|湘|粤|琼|川|贵|云|藏|陕|甘|青|蒙|桂|宁|新|港|澳|台";
        String icpRegexPattern = "(?:" + provincesString + ")ICP备\\s*\\d+号(?:-\\d+)?";
        Pattern icpRegx = Pattern.compile(icpRegexPattern);
        Matcher matcher = icpRegx.matcher(body);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
    public Banner getBanner(String targetURL) throws IOException {
        HttpResponse response = HttpRequest.get(targetURL).execute();
        String body=response.body();
        String title = StrUtil.subBefore(StrUtil.subAfter(body, "<title>", true), "</title>", true);
        Banner banner=new Banner();
        banner.setProtocol(response.httpVersion());
        banner.setHeader(response.headers().toString());
        banner.setTitle(title);
        banner.setBody(body);
        banner.setResponse(response.toString());
        banner.setHash(getHash(body));
        banner.setICP(getICP(body));
        IconUtils iconUtils=new IconUtils();
        banner.setIcon(iconUtils.getIcon(targetURL,body));

        if(targetURL.contains("https:")){
//            HttpsURLConnection conn = response.httpConnection.conn;
//            Certificate[] certs = null;
//            try {
//                certs = conn.getServerCertificates();
//            } catch (SSLPeerUnverifiedException e) {
//                banner.setCert("");
//            }
//            for (Certificate cert : certs) {
//                if (cert instanceof X509Certificate) {
//                    X509Certificate x509Cert = (X509Certificate) cert;
//                    banner.setCert(x509Cert.toString());
//                    System.out.println("Certificate Details:");
//                    System.out.println("IssuerDN: " + x509Cert.getIssuerDN());
//                    System.out.println("SubjectDN: " + x509Cert.getSubjectDN());
//                    System.out.println("SerialNumber: " + x509Cert.getSerialNumber());
//                    System.out.println("Valid From: " + x509Cert.getNotBefore());
//                    System.out.println("Valid To: " + x509Cert.getNotAfter());
//                    // 其他证书详细信息...
//                }
//            }

        }
        return banner;
    }
}
