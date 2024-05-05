package com.rabbitq;

import com.rabbitq.Entity.Banner;
import com.rabbitq.Entity.Finger;
import com.rabbitq.util.BannerUtils;
import com.rabbitq.util.AppFinger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FingerMain {
    public static void main(String[] args) throws Exception {
        printBanner();
        long startTime = System.currentTimeMillis();
        String fingerPath = System.getProperty("user.dir") + "\\fingerprint.txt";
        AppFinger appFinger = new AppFinger();
        List<Finger> fingers = appFinger.InitDatabaseFS(fingerPath);
        //System.out.println(JSON.toJSONString(fingers));
        BannerUtils bannerUtils = new BannerUtils();
        Banner banner = bannerUtils.getBanner(args[0]);
        Set<String> setProductName = new HashSet<>();
        for (Finger finger : fingers) {
            String tempName = appFinger.search(finger, banner);
            if (!tempName.isEmpty()) {
                setProductName.add(tempName);
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\033[32m[*]\033[0m代码运行时间：" + totalTime + "毫秒");
        if(setProductName.size() > 0){
            System.out.println("\033[32m[*]\033[0m已匹配的指纹："+setProductName);
        }
        else {
            System.out.println("\033[31m未匹配到任何指纹");
        }


//        banner.Icon = getIcon(*URL, body);
//        banner.ICP = getICP(body);

    }
    public static void printBanner() {
        System.out.println(
                "██████╗ ██╗   ██╗    ██████╗  █████╗ ██████╗ ██████╗ ██╗████████╗ ██████╗ \n" +
                "██╔══██╗╚██╗ ██╔╝    ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██║╚══██╔══╝██╔═══██╗\n" +
                "██████╔╝ ╚████╔╝     ██████╔╝███████║██████╔╝██████╔╝██║   ██║   ██║   ██║\n" +
                "██╔══██╗  ╚██╔╝      ██╔══██╗██╔══██║██╔══██╗██╔══██╗██║   ██║   ██║▄▄ ██║\n" +
                "██████╔╝   ██║       ██║  ██║██║  ██║██████╔╝██████╔╝██║   ██║   ╚██████╔╝\n" +
                "╚═════╝    ╚═╝       ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═════╝ ╚═╝   ╚═╝    ╚══▀▀═╝ \n" +
                "                                                                          ");
    }


}