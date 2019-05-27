package com.github.chaijunkun.captcha.service;

import java.awt.image.BufferedImage;

import com.github.chaijunkun.captcha.params.CaptchaVerifyParam;

/**
 * 验证码服务，核心的验证码生成及验证逻辑
 * @author chaijunkun
 */
public interface CaptchaService {
	
	/**
	 * 生成验证码令牌
	 * @return
	 */
	String genToken();
	
	/**
	 * 校验token
	 * @param token 被校验的token
	 * @return token中的验证码字符
	 * @throws Exception
	 */
	String getCaptcha(String token) throws Exception;

	/**
	 * 验证输入的验证码与令牌是否匹配
	 * @param param 验证码参数
	 * @return 匹配结果
	 * @throws Exception 匹配时产生的异常
	 */
	boolean doVerify(CaptchaVerifyParam param) throws Exception;
	
	/**
	 * 根据指定的captcha生成相应的验证码图片
	 * @param captcha
	 * @return
	 */
	BufferedImage getCaptchaImg(String captcha);
	
}
