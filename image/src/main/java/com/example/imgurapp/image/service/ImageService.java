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

/**
 * A service layer application responsible for managing image-related applications
 * */
@Slf4j
@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImgurAPIService imgurAPIService;

    /**
     * Uploads an image to Imgur and stores the image details in the database
     *
     * @param user - The user associated with the image
     * @param file - The image file to be uploaded
     * @return The saved image object after successful upload.
     * */
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

    /**
     * Fetches all images associated with a specific user
     *
     * @param user The user whose images needs to be fetched
     * @return A list of images associated with the user
     * */
    public List<Image> getImagesByUser(User user) {
        log.info("Fetching images for user: {}", user.getUserName());
        return user.getImages();
    }

    /**
     * Deletes an image from both - Imgur platform and the database
     *
     * @param image The image to be deleted
     * */
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

    /**
     * Retrieves details of a specific image from the Imgur API
     *
     * @param imageId The ID of the image whose details are to be fetched
     * @return A map consisting the image details
     * */
    public Map<String, Object> getImageDetails(String imageId) {
        log.info("Fetching of Image with image id: {}", imageId);
        return imgurAPIService.getImageDetails(imageId);
    }
}
