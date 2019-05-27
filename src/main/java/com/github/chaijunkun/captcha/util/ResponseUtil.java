package com.github.chaijunkun.captcha.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * 响应组件
 *
 * @author chaijunkun
 */
public class ResponseUtil {

    private static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    public static boolean sendMessageNoCache(HttpServletResponse response, String message) {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        StringBuffer sb = new StringBuffer();
        try {
            PrintWriter writer = response.getWriter();
            sb.append(message);
            writer.write(sb.toString());
            response.flushBuffer();
            return true;
        } catch (Exception e) {
            logger.error("直接推送字符串错误", e);
            return false;
        }
    }

    public static boolean sendJSON(HttpServletResponse response, Object obj) {
        response.setContentType("text/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        StringBuffer sb = new StringBuffer();
        try {
            PrintWriter writer = response.getWriter();
            sb.append(JSONUtil.toJSON(obj));
            writer.write(sb.toString());
            response.flushBuffer();
            return true;
        } catch (Exception e) {
            logger.error("直接推送JSON数据错误", e);
            return false;
        }
    }

    public static boolean sendJSONP(HttpServletResponse response, String callBack, Object obj) {
        response.setContentType("application/javascript;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        StringBuffer sb = new StringBuffer();
        try {
            PrintWriter writer = response.getWriter();
            sb.append(JSONUtil.toJSONP(callBack, obj));
            writer.write(sb.toString());
            response.flushBuffer();
            return true;
        } catch (Exception e) {
            logger.error("直接推送JSONP数据错误", e);

            return false;
        }
    }

    public static boolean sendImg(HttpServletResponse response, BufferedImage buffImg, String mimeType, String fileName, String extName) {
        OutputStream out = null;
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s.%s", fileName, extName));
        try {
            out = response.getOutputStream();
            ImageIO.write(buffImg, extName, out);
//			BufferedInputStream buffIn= new BufferedInputStream(in);
//			BufferedOutputStream buffOut= new BufferedOutputStream(out);
//			byte buff[]= new byte[4096];//申请缓冲区
//			int size= buffIn.read(buff);//初始化读取缓冲
//			while (size!= -1) {
//				buffOut.write(buff, 0, size);//输出缓冲数据
//				size= buffIn.read(buff);//继续读取数据
//			}
//			buffIn.close();//关闭输入流
//			buffOut.flush();//清空输出流
//			buffOut.close();//关闭输出流
            return true;
        } catch (IOException e) {
            logger.error("直接推送图片错误", e);
            return false;
        }
    }

}
