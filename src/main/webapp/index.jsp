<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>基于缓存的验证码框架demo</title>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="js/captcha.js"></script>
</head>
<body>
	<div class="box">
		<dl>
			<dt>验证码</dt>
			<dd>
				<input type="text" id="captcha" name="captcha" />
				<input type="hidden" id="token" name="token" />
			</dd>
		</dl>
		<dl class="fn-mt20">
			<dt style="line-height: 40px;">图片</dt>
			<dd>
				<img src="" alt="正在载入验证码" id="captchaImg" onclick="Captcha.getToken();" style="cursor: pointer;" />
			</dd>
		</dl>
		<dl>
			<dd class="fn-mt20 fn-tac">
				<input class="fn-mr20" type="submit" onclick="Captcha.doVerify();" value="提交" />
				<input type="reset" onclick="Captcha.clear();" value="重置" />
			</dd>
		</dl>
	</div>
	<script type="text/javascript">
		Captcha.getToken();
	</script>
</body>
</html>