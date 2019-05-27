/**
 * 
 */
package com.github.chaijunkun.captcha.params;

/**
 * 校验验证码的响应结果定义
 * @author chaijunkun
 * @since 2016年8月1日
 */
public class CaptchaVerifyResp {
	
	/** 返回代码 成功 */
	public static final int CODE_OK = 0;
	
	/** 返回代码 失败 */
	public static final int CODE_ERR = 1;
	
	/** 验证结果代码 */
	private Integer code;
	
	/** 验证消息 */
	private String msg;

	/**
	 * 获取验证结果代码
	 * @return 验证结果代码
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * 设置验证结果代码
	 * @param code 验证结果代码
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * 获取验证消息
	 * @return 验证消息
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 设置验证消息
	 * @param msg 验证消息
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
