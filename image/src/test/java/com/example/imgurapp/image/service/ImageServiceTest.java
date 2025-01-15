package com.example.imgurapp.image.service;

import com.example.imgurapp.image.entity.Image;
import com.example.imgurapp.image.entity.User;
import com.example.imgurapp.image.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImgurAPIService imgurAPIService;

    @InjectMocks
    private ImageService imageService;

    @Mock
    private User user;

    @Mock
    private MultipartFile multipartFile;

    @Test
    public void testUploadImageSuccess() throws Exception {
        String url = "https://imgur.com/abc123";
        String imageId = "image123";

        Map<String, Object> imgurResponse = new HashMap<>();
        imgurResponse.put("link", url);
        imgurResponse.put("id", imageId);

        when(imgurAPIService.uploadImage(multipartFile)).thenReturn(imgurResponse);
        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Image uploadedImage = imageService.uploadImage(user, multipartFile);

        assertNotNull(uploadedImage);
        assertEquals(url, uploadedImage.getUrl());
        assertEquals(imageId, uploadedImage.getImageId());
        verify(imageRepository, times(1)).save(any(Image.class));
    }


}
