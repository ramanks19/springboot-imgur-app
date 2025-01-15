package com.example.imgurapp.image.service;

import com.example.imgurapp.image.entity.Image;
import com.example.imgurapp.image.entity.User;
import com.example.imgurapp.image.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImgurAPIService imgurAPIService;

    public Image uploadImage(User user, MultipartFile file) {
        log.info("Uploading image for user: {}", user.getUserName());
        try{
            Map<String, Object> imgurResponse = imgurAPIService.uploadImage(file);
            String url = (String) imgurResponse.get("link");
            String imageId = (String) imgurResponse.get("id");

            Image image = new Image();
            image.setUrl(url);
            image.setImageId(imageId);
            image.setUser(user);

            Image savedImage = imageRepository.save(image);
            log.info("Image is uploaded successfully with image ID: {}", savedImage.getId());
            return savedImage;
        } catch (Exception e) {
            log.error("Error while uploading image for user: {}", user.getUserName(), e);
            throw new RuntimeException("Image uploaded FAILED");
        }
    }

    public List<Image> getImagesByUser(User user) {
        log.info("Fetching images for user: {}", user.getUserName());
        return user.getImages();
    }

    public void deleteImage(Image image) {
        log.info("Deleting image with image ID: {}", image.getId());
        try {
            imgurAPIService.deleteImage(image.getImageId());
            imageRepository.delete(image);
            log.info("Image deleted successfully");
        } catch (Exception e) {
            log.error("Unable to delete image with image id: {}", image.getId(), e);
            throw new RuntimeException("Deletion of image unsuccessful.");
        }

    }

    public Map<String, Object> getImageDetails(String imageId) {
        log.info("Fetching of Image with image id: {}", imageId);
        return imgurAPIService.getImageDetails(imageId);
    }
}
