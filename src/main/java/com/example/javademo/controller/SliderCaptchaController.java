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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.awt.geom.Arc2D; // 导入 Arc2D 类

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

        // 创建一个新的背景图片，将滑块区域设置为透明
        BufferedImage maskedBackgroundImage = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = maskedBackgroundImage.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, null);
        g2d.setComposite(AlphaComposite.Clear);

        // 定义滑块形状：整体是方形，左边凹进去一个小半圆，下边突出来一个小半圆
        GeneralPath path = new GeneralPath();
        int radius = 20; // 小半圆的半径

        // 左边凹进去的半圆（位于左边中心）
        int leftCenterY = y + captchaHeight / 2; // 左边中心点的 Y 坐标
        path.moveTo(x, y); // 起点：左上角
        path.lineTo(x, leftCenterY - radius); // 向下移动到凹进去的半圆起点
        path.append(new Arc2D.Double(x - radius, leftCenterY - radius, 2 * radius, 2 * radius, 90, 180, Arc2D.OPEN), true); // 左边凹进去的半圆
        path.lineTo(x, y + captchaHeight); // 向下移动到左下角

        // 下边突出来的半圆（位于下边中心）
        int bottomCenterX = x + captchaWidth / 2; // 下边中心点的 X 坐标
        path.lineTo(bottomCenterX - radius, y + captchaHeight); // 向右移动到突出来的半圆起点
        path.append(new Arc2D.Double(bottomCenterX - radius, y + captchaHeight - radius, 2 * radius, 2 * radius, 180, 180, Arc2D.OPEN), true); // 下边突出来的半圆
        path.lineTo(x + captchaWidth, y + captchaHeight); // 向右移动到底部右侧

        // 闭合路径
        path.lineTo(x + captchaWidth, y); // 向上移动到右上角
        path.lineTo(x, y); // 向左移动到起点
        path.closePath(); // 闭合路径

        g2d.fill(path); // 填充路径，形成透明区域
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.dispose();

        // 创建一个新的滑块图片，形状与背景中的透明区域一致
        BufferedImage sliderImage = new BufferedImage(captchaWidth, captchaHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sliderG2d = sliderImage.createGraphics();

        // 将滑块的路径平移到滑块图片的坐标系
        GeneralPath sliderPath = new GeneralPath(path);
        sliderPath.transform(AffineTransform.getTranslateInstance(-x, -y)); // 平移路径

        // 设置滑块图片的裁剪区域
        sliderG2d.setClip(sliderPath);

        // 从背景图中截取滑块区域的图像，并绘制到滑块图片中
        BufferedImage sliderSubImage = backgroundImage.getSubimage(x, y, captchaWidth, captchaHeight);
        sliderG2d.drawImage(sliderSubImage, 0, 0, null);
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