package usrportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
    basePackages = {"security.config", "security.config.oauth", "usrportal", "usrportal.config",
        "usrportal.email", "usrportal.repo", "usrportal.rest", "usrportal.ui", "usrportal.utils"})
@EnableJpaRepositories
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
