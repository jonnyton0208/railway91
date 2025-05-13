package vti.academy.railway91;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"Controller", "Service", "Security", "Repository", "vti.academy.railway91"})
@EntityScan(basePackages = {"Entity"})
@EnableJpaRepositories(basePackages = {"Repository"})
public class Railway91Application {

    public static void main(String[] args) {
        SpringApplication.run(Railway91Application.class, args);
    }

}
