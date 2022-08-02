package graph_encryption;

import graph_encryption.service.ImageService;
import graph_encryption.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * This class configures the unit tests
 */
@Profile("test")
@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public ImageService imageService() {
        return Mockito.mock(ImageService.class);
    }

    @Bean
    @Primary
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }
}