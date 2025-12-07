package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileController {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    @Value("${server.port:8080}")
    private String serverPort;

    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("File is empty");
        }

        try {
            // Create upload directory if not exists
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;

            // Save file
            // Use absolute path to avoid issue where transferTo resolves relative paths against temp dir
            File dest = new File(dir.getAbsolutePath() + File.separator + filename);
            file.transferTo(dest);

            // Generate URL
            // Note: In production, this should be a domain name or configured base URL
            // For local dev, we use localhost IP (hardcoded for now as getting actual IP is
            // complex)
            // or relative path if frontend handles base URL.
            // Let's return a relative path and let frontend prepend base URL, or return
            // full URL.
            // Since frontend config has base URL, returning relative path might be safer,
            // but the requirement implies a full URL. Let's try to construct a usable URL.
            // Ideally, we should use the request to get the scheme/host/port, but for now:
            String url = "/uploads/" + filename;

            Map<String, String> result = new HashMap<>();
            result.put("url", url);

            return Result.success(result);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("Upload failed: " + e.getMessage());
        }
    }
}
