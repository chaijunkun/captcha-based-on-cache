package com.github.chaijunkun.captcha.util;

/**
 * JSON序列化产生的异常
 * @author chaijunkun
 * @since 2014年11月28日
 */
public class JSONSerException extends JSONException {

    /**
     *
     */
    private static final long serialVersionUID = -8008709226942094375L;

    public JSONSerException(Throwable cause){
        super("JSON Serialize Error", cause);
    }

}
