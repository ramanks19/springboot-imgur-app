# Image Upload Application
This application is a Spring Boot-based RESTFUL service thar allows users to upload images to Imgur, associate them with their accounts, and manage user profiles.

## Features
- User registration and authentication
- RESTful APIs for upload, view and delete image
- Unit tests for services 
- H2 in-memory database for development

### Technologies Used:
- **Java**: Version 17
- **Spring Boot**: 3.x.x
- **Database**: H2 Database
- **API's Used**: Imgur Image API
- **Build Tool**: Maven
- **Testing Framework**: JUnit

### Installation
#### Clone the repository:
```
git clone https://github.com/ramanks19/springboot-imgur-app.git
```

#### Build the project:
```
mvn clean install
```

#### Run the Spring Boot application:
```
mvn spring-boot:run
```

### API Endpoints
#### User APIs
- /api/users/register - A POST request to create a User
- /api/users/authenticate - A POST request to authenticate a user 
- /api/users/{userName} - A GET request to retrieve the user profile

#### Image APIs
- /api/images/upload - A POST request to upload an Image
- /api/images/user/{userName} - A GET request to obtain all the images associated with the User
- /api/images/delete/{imageId} - A DELETE request to delete an image using its image ID obtained from Imgur

### Important Points
Before accessing the Imgur APIs you need to obtain the client-ID and client-secret for your application from Imgur. For more information you can visit - https://apidocs.imgur.com/#intro

### Points of Improvement
Since, this is a very basic example of how to use Imgur API, it can be enhanced further by:
- Obtaining access tokens and refresh tokens to enable the tagging of each image to their users.
- Using Spring Security to enhance the security features.
- Only basic Junit test cases has been added. More functionalities can be added to have a more robust testing framework.

