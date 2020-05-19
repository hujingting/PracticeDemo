package com.tutao.common.network;

/**
 * Created by apple on 16/11/30.
 */

public class NameValuePair {
    private String value;
    private String name;
    private String alias="";

    private boolean isFile=false;//是否是文件

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlias(){return alias;}

    public void setAlias(String alias){this.alias = alias;}

    public NameValuePair(String name, String value){
        this.name=name;
        this.value = value;
    }

    public NameValuePair(String name, String value, String alias){
        this.name=name;
        this.value = value;
        this.alias = alias;
    }

    public NameValuePair(String name, String value, boolean isFile){
        this.name=name;
        this.value = value;
        this.isFile = isFile;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
