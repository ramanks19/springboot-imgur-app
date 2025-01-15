package com.example.imgurapp.image.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class ImgurAPIService {

    @Value("${imgur.client.id}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Client-ID" + clientId);
        return headers;
    }

    public Map<String, Object> uploadImage(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            String encodedFile = Base64.getEncoder().encodeToString(fileBytes);

            HttpHeaders headers = buildHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = Map.of("image", encodedFile);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            log.info("Image upload request sent to Imgur");
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.imgur.com/3/image", HttpMethod.POST, request, Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Image uploaded successfully to Imgur.");
                Map<String, Object> responseBody = response.getBody();
                return (Map<String, Object>) responseBody.get("data");
            } else {
                log.error("Image upload failed with status: {}", response.getStatusCode());
                throw new RuntimeException("Failed to upload image to Imgur");
            }
        } catch (Exception e) {
            log.error("Error during image upload operation to Imgur.", e);
            throw new RuntimeException("Error during Imgur upload operation", e);
        }
    }

    public void deleteImage(String imageId) {
        try {
            HttpHeaders headers = buildHeaders();
            HttpEntity<Void> request = new HttpEntity<>(headers);

            log.info("An image delete request has been sent out for Imgur image Id: {}", imageId);
            ResponseEntity<Void> response = restTemplate.exchange(
                    "https://api.imgur.com/3/image/" + imageId, HttpMethod.DELETE, request, Void.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("Deletion of image failed with status: {}", response.getStatusCode());
                throw new RuntimeException("Failed to delete image from imgur");
            } else {
                log.info("Image deleted successfully.");
            }
        } catch (Exception e) {
            log.error("Error during image deletion operation", e);
            throw new RuntimeException("Error thrown during deletion", e);
        }
    }

    public Map<String, Object> getImageDetails(String imageId) {
        try {
            HttpHeaders headers = buildHeaders();
            HttpEntity<Void> request = new HttpEntity<>(headers);

            log.info("Fetching image details for Imgur image id: {}", imageId);
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.imgur.com/3/image/" + imageId, HttpMethod.GET, request, Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Image details fetched successfully.");
                return (Map<String, Object>) response.getBody().get("data");
            } else {
                log.error("Failed to retrieve image details with status code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to retrieve image details from Imgur");
            }
        } catch (Exception e) {
            log.error("Error during fetching image information", e);
            throw new RuntimeException("Error during fetching", e);
        }
    }
}
