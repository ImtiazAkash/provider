package com.service.provider.controller;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/file")
public class FileController {

    private final ResourceLoader resourceLoader;

    public FileController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/images/{imageName}")
    @ResponseBody
    public Resource getImage(@PathVariable String imageName) {
        return resourceLoader.getResource("classpath:static/assests/images/" + imageName);
    }
}
