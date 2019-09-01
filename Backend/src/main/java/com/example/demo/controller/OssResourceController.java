package com.example.demo.controller;

import com.example.demo.model.OssResource;
import com.example.demo.service.CloudStorageService;
import com.example.demo.service.OssResourceService;
import com.example.demo.util.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class OssResourceController {

    @Autowired
    private OssResourceService ossResourceService;
    @Autowired
    private CloudStorageService cloudStorageService;

    @PostMapping("/upload")
    public Result uploadCover(@NotNull MultipartFile file) throws Exception {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = cloudStorageService.uploadSuffix(file.getBytes(), suffix);
        OssResource resource = new OssResource(url, file.getOriginalFilename());
        ossResourceService.save(resource);
        return Result.ok().put("resource", resource);
    }
}
