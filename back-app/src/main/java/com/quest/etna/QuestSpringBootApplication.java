package com.quest.etna;

import com.quest.etna.model.FileStorageProperties;
import com.quest.etna.model.Product;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.service.ProductService;
import com.quest.etna.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class QuestSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuestSpringBootApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ProductService productService, UserService userService) {
        return args -> {
            productService.save(new Product("Harry Potter and the Chamber of Secrets", "Description Product 1", (double) 20));
            productService.save(new Product("After", "Description Product 2", (double) 21));
            productService.save(new Product("In Search of Lost Time", "Description Product 3", (double) 22));
            productService.save(new Product("Ulysses", "Description Product 4", (double) 23));
            //userService.save(new User("skan", "skander", UserRole.ROLE_ADMIN));
            //userService.save(new User("user", "password", UserRole.ROLE_USER));
        };
    }

}
