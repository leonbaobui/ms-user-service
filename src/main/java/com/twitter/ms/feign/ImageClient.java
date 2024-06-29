package com.twitter.ms.feign;

import com.gmail.merikbest2015.configuration.FeignConfiguration;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import static com.gmail.merikbest2015.constants.FeignConstants.IMAGE_SERVICE;
import static com.gmail.merikbest2015.constants.FeignConstants.USER_SERVICE;
import static com.gmail.merikbest2015.constants.PathConstants.API_V1_IMAGE;

@FeignClient(value = IMAGE_SERVICE, url = "${service.downstream-url.ms-image-service}", path = "/" + IMAGE_SERVICE + API_V1_IMAGE, configuration = FeignConfiguration.class)
public interface ImageClient {

    @CircuitBreaker(name = IMAGE_SERVICE)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadImageUserProfile(@RequestPart(value = "file") MultipartFile file);
}
