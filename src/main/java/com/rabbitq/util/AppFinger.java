package com.rabbitq.util;

import cn.hutool.core.io.file.FileReader;
import com.rabbitq.Entity.Banner;
import com.rabbitq.Entity.Expression;
import com.rabbitq.Entity.Finger;
import com.rabbitq.Entity.Param;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;


public class AppFinger {


    public List<Finger> InitDatabaseFS(String fingerPath) throws Exception {
        FileReader fileReader = new FileReader(fingerPath);
        List<String> listFileLine = fileReader.readLines();
        int lineNumber = 0;
        IOException lastError = null;
        List<Finger> listFinger = new ArrayList<>();
        int count = 0;
        for (String line : listFileLine) {
            lineNumber++;
            line = line.trim();
            String[] arrSingleFinger = line.split("\t", 2);
            if (arrSingleFinger.length != 2) {
                System.out.println("Invalid line format: " + line);
                continue;
            }
            String strProDuctName = arrSingleFinger[0];
            String keyword = arrSingleFinger[1];
            listFinger.add(parseFingerPrint(strProDuctName, keyword));
            count++;
        }

        if (lastError != null) {
            throw lastError; // Re-throw the last error after processing all lines
        }

        return listFinger;
    }

    public Finger parseFingerPrint(String ProductName, String strExpression) throws Exception {
        Finger finger = new Finger();
        finger.setProDuctName(ProductName);
        finger.setKeyword(parseExpression(strExpression));
        return finger;
    }

    public Expression parseExpression(String expr) throws Exception {
        Expression e = new Expression();
        e.setValue(expr.trim());
        String temporaryPlaceholder = "[quota]";

        // 替换双引号为临时占位符
        // 修饰expr
        expr = expr.replaceAll("\\\\\"", temporaryPlaceholder);


        // 字符合法性校验
//        if (!exprCharVerification(expr)) {
//            throw new Exception("Invalid characters in expression.");
//        }

        // 提取param数组
        List<Param> paramSlice = new ArrayList<>();
        //Pattern paramPattern = Pattern.compile(KEYWORD_PATTERN); // 替换为您的参数正则表达式
        Matcher paramMatcher = paramRegx.matcher(expr);
        int index = 0;

        // 对param进行解析
        while (paramMatcher.find()) {
            String value = paramMatcher.group();
            //expr = expr.replaceFirst(Pattern.quote(value), "${" + (index + 1) + "}");
            Param param = parseParam(value);
            if (param == null) {
                throw new Exception("Failed to parse parameter.");
            }
            paramSlice.add(param);
            index++;
        }

        // 语义合法性校验
        if (!exprSyntaxVerification(expr)) {
            throw new Exception("Syntax verification failed.");
        }

        e.setExpr(expr.replaceAll("\\[quota\\]", "\\\""));
        e.setParamSlice(paramSlice);
        return e;
    }

    private final Pattern paramRegx = Pattern.compile("([a-zA-Z0-9]+) *(!=|=|~=|==) *\"([^\"\\n]+)\""); // 替换为您的关键字正则表达式
    private final List<String> listKeywordSlice = new ArrayList<String>() {{
        add("Title");
        add("Header");
        add("Body");
        add("Response");
        add("Protocol");
        add("Cert");
        add("Port");
        add("Hash");
        add("Icon");
    }}; // 替换为您的关键字正则表达式

    public Param parseParam(String expr) throws Exception {
        Matcher keywordMatcher = paramRegx.matcher(expr);
        if (!keywordMatcher.find()) {
            throw new Exception("Keyword not found in expression.");
        }

        String keyword = keywordMatcher.group(1);
        String valueRaw = keywordMatcher.group(3);

        keyword = keyword.substring(0, 1).toUpperCase() + keyword.substring(1);
        if (!listKeywordSlice.contains(keyword)) {
            throw new Exception("Unknown keyword: " + keyword);
        }

        String operator = convOperator(keywordMatcher.group(2));

        valueRaw = valueRaw.replaceAll("\\[quota\\]", "\\\"");
//        String value;
//        try {
//            value = new String(Pattern.compile(valueRaw).pattern());
//        } catch (Exception e) {
//            throw new Exception("Invalid regular expression value: " + valueRaw, e);
//        }

        return new Param(keyword, valueRaw, operator);
    }

    private String convOperator(String operatorStr) {
        // 实现操作符转换逻辑
        // ...
        return operatorStr; // 假设转换成功
    }

    private boolean exprSyntaxVerification(String expr) {
        // 实现表达式语义校验逻辑
        // ...
        return true; // 假设校验成功
    }

    private boolean exprCharVerification(String expr) throws Exception {
        // 把所有param替换为空
        String str = expr.replaceAll("your_param_regex_here", ""); // 替换为您的参数正则表达式

        // 把所有逻辑字符替换为空
        str = str.replaceAll("[&| ()]", "");

        // 检测是否存在其他字符
        if (!str.isEmpty()) {
            str = str.replaceAll("\\[quota\\]", "\"");
            throw new Exception(str + " is unknown");
        }

        // 检测语法合法性
        return true;
    }

    public String search(Finger finger, Banner banner) {
        boolean boolResult = true;
        List<Param> paramSlice = finger.getKeyword().getParamSlice();
        Param tempParam = null;
        for (Param param : paramSlice) {
            if (!match(param, banner)) {
                boolResult = false;
                break;
            }

            tempParam = param;
        }
        if (boolResult) {
            match(tempParam, banner);

            return finger.getProDuctName();
        }
        return "";
    }

    public String getKeywordValue(String keyword, Banner banner) {
        Map<String, Getter> getters = new HashMap<>();
        getters.put("Protocol", banner::getProtocol);
        getters.put("Title", banner::getTitle);
        getters.put("Port", banner::getPort);
        getters.put("Header", banner::getHeader);
        getters.put("Body", banner::getBody);
        getters.put("Response", banner::getResponse);
        getters.put("Cert", banner::getCert);
        getters.put("Hash", banner::getHash);
        getters.put("Icon", banner::getIcon);
        getters.put("ICP", banner::getICP);

        // ... 添加其他属性的getter方法 ...
        Getter getter = getters.get(keyword);
        if (getter != null) {
            return getter.get();
        } else {
            return "";
        }
    }

    public Boolean match(Param param, Banner banner) {
        String keyword = param.getKeyword();
        String strOperator = param.getOperator();
        String subStr = param.getValue();
        String value = getKeywordValue(keyword, banner);
        if (value == null || value.equals("")) {
            return false;
        }
        switch (strOperator) {
            case "!=":
                return !value.contains(subStr);
            case "=":
                return value.contains(subStr);
            case "~=":
                try {
                    long startTime = System.currentTimeMillis();

                    Pattern pattern = Pattern.compile(subStr);
                    Matcher matcher = pattern.matcher(value);
                    boolean boolResult = matcher.find();
                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;
//                    System.out.println("代码运行时间：" + totalTime + "毫秒");
                    if (totalTime > 1000) {
                        System.out.println(subStr);
                    }
                    return boolResult;
                } catch (Exception e) {
//                    System.out.println(subStr);
//                    System.out.println("-------------"+e.getMessage()+"-------------");
//                    System.out.println();
//                    System.out.println();
                    return false;
                }

            case "==":
                return value.equals(subStr);
            default:
                return false;
        }
    }

}
