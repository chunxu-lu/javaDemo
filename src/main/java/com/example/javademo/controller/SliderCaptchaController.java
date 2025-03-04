package com.example.javademo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/captcha")
public class SliderCaptchaController {

    @Value("${slider.captcha.width}")
    private int captchaWidth;

    @Value("${slider.captcha.height}")
    private int captchaHeight;

    @Value("${slider.captcha.sliderBlockGap}")
    private int sliderBlockGap;

    @GetMapping("/slider")
    public ResponseEntity<Map<String, String>> getSliderCaptcha() throws IOException {
        BufferedImage backgroundImage = ImageIO.read(new ClassPathResource("static/images/background.png").getInputStream());

        int bgWidth = backgroundImage.getWidth();
        int bgHeight = backgroundImage.getHeight();

        if (bgWidth - captchaWidth - sliderBlockGap <= 0 || bgHeight - captchaHeight <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Random random = new Random();
        int x = random.nextInt(bgWidth - captchaWidth - sliderBlockGap);
        int y = random.nextInt(bgHeight - captchaHeight);

        // 截取滑块图片
        BufferedImage sliderImage = backgroundImage.getSubimage(x, y, captchaWidth, captchaHeight);

        // 创建一个新的背景图片，将滑块区域设置为透明
        BufferedImage maskedBackgroundImage = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = maskedBackgroundImage.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, null);
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(x, y, captchaWidth, captchaHeight);
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.dispose();

        // 在滑块图片上绘制不规则形状的凹凸部分
        Graphics2D sliderG2d = sliderImage.createGraphics();
        sliderG2d.setColor(new Color(255, 255, 255, 0)); // 设置透明色

        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        path.curveTo(captchaWidth / 4, captchaHeight / 4, captchaWidth / 2, captchaHeight / 2, captchaWidth, 0);
        path.lineTo(captchaWidth, captchaHeight);
        path.lineTo(0, captchaHeight);
        path.closePath();

        sliderG2d.fill(path);
        sliderG2d.dispose();

        // 将背景图片和滑块图片转换为Base64编码的字符串
        String backgroundBase64 = imageToBase64(maskedBackgroundImage);
        String sliderBase64 = imageToBase64(sliderImage);

        // 封装成JSON响应
        Map<String, String> response = new HashMap<>();
        response.put("background", backgroundBase64);
        response.put("slider", sliderBase64);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    private String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }
}