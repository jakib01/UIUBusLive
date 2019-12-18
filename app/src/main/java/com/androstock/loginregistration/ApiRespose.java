package com.androstock.loginregistration;

public class ApiRespose {

    int code;
    String message;
    boolean data;

    public ApiRespose(int c, String m, boolean b) {
        this.code = c;
        this.message = m;
        this.data = b;
    }
}
