package com.rabbitq.Entity;

public class Param {
    private String keyword;
    private String value;
    private String operator;

    public Param(String keyword, String value, String operator) {
        this.keyword = keyword;
        this.value = value;
        this.operator = operator;
    }
    // getters and setters

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
