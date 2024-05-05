package com.rabbitq.util;

import cn.hutool.core.util.HashUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IconUtils {
    private static final Pattern regxIconPath = Pattern.compile("^/.*$"); // 需要替换为实际的正则表达式
    private static final Pattern regxIconURL = Pattern.compile("^(?:http|https)://.*$"); // 需要替换为实际的正则表达式
    private static final Pattern regxIconURLNotProtocol = Pattern.compile("^://.*$"); // 需要替换为实际的正则表达式

    public String getIcon(String targetUrl, String body)  {
        URL url= null;
        try {
            url = new URL(targetUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String path = getIconPath(body);
        if (isMatched(regxIconPath, path)) {
            return getIconHash(url.getProtocol() + "://" + url.getHost() + path);
        }
        if (isMatched(regxIconURL, path)) {
            return getIconHash(path);
        }
        if (isMatched(regxIconURLNotProtocol, path)) {
            return getIconHash(url.getProtocol() + ":" + path);
        }
        return "";
    }
    private String getIconPath(String body) {
        try {
            Document document = Jsoup.parse(body);
            Elements links = document.select("link[rel='shortcut icon']");
            for (Element link : links) {
                String href = link.attr("href");
                if (!href.isEmpty()) {
                    return href;
                }
            }
        } catch (Exception e) {
            // Handle parsing error
        }
        return "/favicon.ico";
    }
    private static String getIconHash(String urlString) {
        HttpResponse response = HttpRequest.get(urlString).execute();
        String body=response.body();
        if (response.isOk()){
            return iconhash(body);
        }
        return "";
    }

    private static boolean isMatched(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    // 这里需要一个实际的iconhash方法
    private static String iconhash(String reader) {
        // 图标哈希编码逻辑
        return Encode(reader.getBytes(StandardCharsets.UTF_8));
    }
    public static String mmh3Hash32(byte[] raw) {
        int hash32 = HashUtil.murmur32(raw);
        return Integer.toString(hash32);
    }

    public static String Encode(byte[] buf) {
        String base64 = Base64.getEncoder().encodeToString(buf);
        return mmh3Hash32(base64.getBytes(StandardCharsets.UTF_8));
    }


}
