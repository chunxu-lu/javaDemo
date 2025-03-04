package com.example.javademo.util;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Base64;

@Component
public class QiniuCloudUtil {

    @Value("${qiniu.access-key}")
    private String accessKey;

    @Value("${qiniu.secret-key}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    private UploadManager uploadManager;

    public QiniuCloudUtil() {
        Configuration cfg = new Configuration(Region.autoRegion());
        uploadManager = new UploadManager(cfg);
    }

    /**
     * 对字符串进行Base64编码
     */
    private String encodeFileName(String fileName) {
        return Base64.getEncoder().encodeToString(fileName.getBytes());
    }

    /**
     * 对Base64编码的字符串进行解码
     */
    private String decodeFileName(String encodedFileName) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedFileName);
        return new String(decodedBytes);
    }

    public String upload(InputStream inputStream, String fileName) throws QiniuException {
        // 对文件名进行Base64编码
        String encodedFileName = encodeFileName(fileName);

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        Response response = uploadManager.put(inputStream, encodedFileName, upToken, null, null);
        if (response.isOK()) {
            // 在返回的URL中使用编码后的文件名
            return domain + "/" + encodedFileName;
        }
        return null;
    }
}