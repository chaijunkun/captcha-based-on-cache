package com.github.chaijunkun.captcha.service.impl;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.github.chaijunkun.captcha.misc.CaptchaConfig;
import com.github.chaijunkun.captcha.params.CaptchaVerifyParam;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.chaijunkun.captcha.service.CaptchaService;
import com.github.chaijunkun.captcha.util.XXTEAUtil;

/**
 * 验证码服务实现
 * @author chaijunkun
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

	private static Logger logger= Logger.getLogger(CaptchaServiceImpl.class);

	private static final String[] codeBase= {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

	private static Random rand= new Random();
	
	@Autowired
	private CaptchaConfig captchaConfig;
	
	public CaptchaServiceImpl(){
		logger.info("正在构建验证码生成与验证服务");
	}

	public CaptchaConfig getCaptchaConfig() {
		return captchaConfig;
	}

	public void setCaptchaConfig(CaptchaConfig captchaConfig) {
		this.captchaConfig = captchaConfig;
	}

	/**
	 * 生成token黑名单缓存key
	 * @param token
	 * @return
	 */
	private String genTokenBlacklistCacheKey(String token){
		return String.format("%s_token_black_%s", this.captchaConfig.getCachePrefix(), token);
	}
	
	/**
	 * 生成token已验证key
	 * @param token
	 * @return
	 */
	private String genVerifiedTokenCacheKey(String token){
		return String.format("%s_token_verified_%s", this.captchaConfig.getCachePrefix(), token);
	}

	@Override
	public String getCaptcha(String token) throws Exception {
		try{
			if (this.captchaConfig.getCacheService().hasKey(genTokenBlacklistCacheKey(token))){
				throw new IllegalStateException("此token已列入黑名单");
			}
			String plainText= XXTEAUtil.decrypt(token, this.captchaConfig.getSecKey());
			if (StringUtils.isBlank(plainText)){
				throw new IllegalStateException("解密失败,token可能遭到篡改");
			}
			String[] plainTextArr= plainText.split("_");
			if (plainTextArr.length!=2){
				throw new IllegalStateException("token数据格式错误");
			}
			long timestamp= 0;
			try{
				timestamp= Long.parseLong(plainTextArr[1]);
			}catch(NumberFormatException e){
				throw new IllegalStateException("时间戳无效");
			}
			if ((System.currentTimeMillis() - timestamp)>TimeUnit.MILLISECONDS.convert(this.captchaConfig.getExpire() + 5, TimeUnit.SECONDS)){
				throw new IllegalStateException("验证码已过期");
			}
			return plainTextArr[0];
		}catch(Exception e){
			this.captchaConfig.getCacheService().set(genTokenBlacklistCacheKey(token), Boolean.TRUE.toString(), this.captchaConfig.getExpire());
			throw new Exception(e.getMessage());
		}
	}


	@Override
	public String genToken() {
		StringBuffer sb= new StringBuffer();
		for(int i=0; i<this.captchaConfig.getCharCount(); i++){
			int randInt= Math.abs(rand.nextInt());
			sb.append(codeBase[randInt % codeBase.length]);
		}
		long timestamp= System.currentTimeMillis();
		String token= null;
		token= String.format("%s_%d", sb.toString(), timestamp);
		logger.info("未加密的token:"+token);
		token= XXTEAUtil.encrypt(token, this.captchaConfig.getSecKey());
		return token;
	}

	@Override
	public boolean doVerify(CaptchaVerifyParam param) throws Exception {
		if (StringUtils.isBlank(param.getCaptcha())){
			throw new IllegalArgumentException("未输入验证码");
		}
		if (StringUtils.isBlank(param.getToken())){
			throw new IllegalArgumentException("token缺失");
		}
		//判断缓存中有没有此token
		//如果有说明已经被验证过了
		if (this.captchaConfig.getCacheService().hasKey(this.genVerifiedTokenCacheKey(param.getToken()))){
			throw new Exception("该验证码已经被验证过");
		}else{
			//如果没有 将其放入到已验证缓存库中 
			this.captchaConfig.getCacheService().set(this.genVerifiedTokenCacheKey(param.getToken()), Boolean.TRUE.toString(), this.captchaConfig.getExpire());
		}
		String verifyCode= getCaptcha(param.getToken());
		verifyCode= verifyCode.toLowerCase();
		String code= param.getCaptcha().toLowerCase();
		if (code.equals(verifyCode)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public BufferedImage getCaptchaImg(String captcha) {
		return this.captchaConfig.getProducer().createImage(captcha);
	}

}
