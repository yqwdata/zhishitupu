package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${server.port}")  // 改用 Spring 标准端口配置
    private String port;
    private static final String ip = "http://localhost";


    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir")
                + "\\springboot\\src\\main\\resources\\files\\"
                + flag + "_"
                + originalFilename;
        FileUtil.writeBytes(file.getBytes(), rootFilePath);

        return Result.success(ip + ":" + port + "/files/" + flag);
    }

    @GetMapping("/{flag}")
    public void getFiles(HttpServletResponse response, @PathVariable String flag) {
        OutputStream os;
        String basePath = System.getProperty("user.dir") + "\\springboot\\src\\main\\resources\\files\\";
        List<String> fileNames = FileUtil.listFileNames(basePath);
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            System.out.println("文件下载失败");
        }
    }

}