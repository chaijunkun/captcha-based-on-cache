/**
 * 
 */
package com.github.chaijunkun.captcha.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.chaijunkun.captcha.service.CaptchaService;

/**
 * 
 * @author chaijunkun
 * @since 2016年8月1日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class CaptchaServiceTest {
	
	private static final Logger log = LoggerFactory.getLogger(CaptchaServiceTest.class);
	
	@Autowired
	private CaptchaService captchaService;
	
	@Test
	public void doTest(){
		log.info("Hello World!");
		String token = captchaService.genToken();
		log.info("token:{}", token);
	}

}
