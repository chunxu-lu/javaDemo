package com.example.javademo.controller;

import com.example.javademo.mapper.BgimgtableMapper;
import com.example.javademo.entity.Bgimgtable;
import com.example.javademo.model.ResponseResult;
import com.example.javademo.util.QiniuCloudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UploadController {
    @Autowired
    private QiniuCloudUtil qiniuCloudUtil;

    @Autowired
    private BgimgtableMapper bgimgtableMapper;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("imgName") String imgName, @RequestParam("flag") String flag) {
        Map<String, Map<String, String>> response = new HashMap<>();
        Map<String, String> msg = new HashMap<>();

        Integer flagInt = Integer.parseInt(flag);

        if (file.isEmpty()) {
            msg.put("msg", "文件不能为空");
//            response.put("data", msg);
//            return response;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseResult<>(HttpStatus.BAD_REQUEST.value(), "文件不能为空", null));
        }

        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String fileUrl = qiniuCloudUtil.upload(inputStream, fileName);
            if (fileUrl != null) {
                if (flagInt == 1) {
                    // 将所有其他记录的flag设置为0
                    bgimgtableMapper.resetAllFlags();
                }
                Bgimgtable bgimgtable = new Bgimgtable();
                bgimgtable.setImgUrl(fileUrl);
                bgimgtable.setImgName(imgName);
                bgimgtable.setFlag(flagInt);
                bgimgtableMapper.insert(bgimgtable);
                msg.put("msg", "上传成功，文件地址：" + fileUrl);
            } else {
                msg.put("msg", "上传失败");
            }
            response.put("data", msg);
//            return response;
            return ResponseEntity.ok(new ResponseResult<>(HttpStatus.OK.value(), "success", msg));
        } catch (IOException e) {
            e.printStackTrace();
            msg.put("msg", "上传失败");
            response.put("data", msg);
//            return response;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "上传失败", null));
        }
    }

    @GetMapping("/getImages")
    public
    ResponseEntity
            <ResponseResult<List<Bgimgtable>>> getAllImages() { // 修改返回类型
        List<Bgimgtable> bgimgs = bgimgtableMapper.selectAll().stream()
                .map(bgimgtable -> {
                    String imgUrl = bgimgtable.getImgUrl();
                    if (!imgUrl.startsWith("https://")) {
                        imgUrl = "https://" + imgUrl; // 修改后的代码
                    }
                    return new Bgimgtable(bgimgtable.getId(), imgUrl, bgimgtable.getImgName(),bgimgtable.getFlag()); // 创建 Bgimgtable 对象并包含 imgName
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ResponseResult<>(HttpStatus.OK.value(), "success", bgimgs));
    }

    @PostMapping("/deleteImage")
    public ResponseEntity<?> deleteImage(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        if (bgimgtableMapper.selectById(id) != null) { // 注意这里需要根据实际情况调整方法名或实现
            bgimgtableMapper.deleteById(id);
//            return ResponseEntity.ok("Image deleted successfully");
            return ResponseEntity.ok(new ResponseResult<>(HttpStatus.OK.value(), "Image deleted successfully", null));
        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseResult<>(HttpStatus.NOT_FOUND.value(), "Image not found", null));
        }
    }
}