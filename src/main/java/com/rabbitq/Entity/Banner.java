package com.rabbitq.Entity;

public class Banner {
    public String Protocol;
    public String  Port;
    public String  Header;
    public String  Body;
    public String  Response;
    public String  Cert;
    public String  Title;
    public String   Hash;
    public String  Icon;

    public String  ICP;


    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String protocol) {
        Protocol = protocol;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getCert() {
        return Cert;
    }

    public void setCert(String cert) {
        Cert = cert;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getICP() {
        return ICP;
    }

    public void setICP(String ICP) {
        this.ICP = ICP;
    }
}
