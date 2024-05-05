package com.rabbitq.Entity;

import java.util.List;

public class Expression {
    private String value;
    private String expr;
    private List<Param> paramSlice;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public List<Param> getParamSlice() {
        return paramSlice;
    }

    public void setParamSlice(List<Param> paramSlice) {
        this.paramSlice = paramSlice;
    }

}
