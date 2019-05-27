/**
 * 
 */
package com.github.chaijunkun.captcha.params;

/**
 * 提交验证码验证参数定义
 * @author chaijunkun
 * @since 2016年8月1日
 */
public class CaptchaVerifyParam {
	
	/** 前端用户输入的验证码 */
	private String captcha;
	
	/** 系统生成的验证token */
	private String token;
	
	/**
	 * 获取前端用户输入的验证码
	 * @return 前端用户输入的验证码
	 */
	public String getCaptcha() {
		return captcha;
	}
	
	/**
	 * 设置前端用户输入的验证码
	 * @param captcha 前端用户输入的验证码
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
	/**
	 * 获取系统生成的验证token
	 * @return 系统生成的验证token
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * 设置系统生成的验证token
	 * @param token 系统生成的验证token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
}
