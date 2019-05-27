package com.github.chaijunkun.captcha.util;

/**
 * JSON反序列化时产生的异常
 * @author chaijunkun
 * @since 2014年12月1日
 */
public class JSONDeserException extends JSONException {

    /**
     *
     */
    private static final long serialVersionUID = -5185553448828334299L;

    public JSONDeserException(Throwable cause){
        super("JSON Deserialize Error", cause);
    }

}
