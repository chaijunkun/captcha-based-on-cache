package com.github.chaijunkun.captcha.util;

/**
 * 所有JSON异常的父类
 * @author chaijunkun
 * @since 2015年4月15日
 */
public abstract class JSONException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 2056347842277681924L;

    public JSONException() {
        super();
    }

    public JSONException(String message, Throwable cause){
        super(message, cause);
    }

}
