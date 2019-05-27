package com.github.chaijunkun.captcha.misc;

import java.awt.image.BufferedImage;

import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.util.Configurable;

/**
 * 波纹干扰线生成器,此处只是一个简单实现，没有添加波纹
 *
 * @author chaijunkun
 */
public class DefaultRipple extends Configurable implements GimpyEngine {

    @Override
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        // TODO Auto-generated method stub
        return baseImage;
    }

}
