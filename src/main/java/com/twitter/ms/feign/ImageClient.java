package com.twitter.ms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import main.java.com.leon.baobui.configuration.FeignConfiguration;

import static main.java.com.leon.baobui.constants.FeignConstants.IMAGE_SERVICE;
import static main.java.com.leon.baobui.constants.PathConstants.API_V1_IMAGE;

@FeignClient(value = IMAGE_SERVICE, url = "${service.downstream-url.ms-image-service}",
        path = "/" + IMAGE_SERVICE + API_V1_IMAGE, configuration = FeignConfiguration.class)
public interface ImageClient {

    @CircuitBreaker(name = IMAGE_SERVICE)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadImageUserProfile(@RequestPart(value = "file") MultipartFile file);
}
