package usrportal;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import usrportal.repo.User;
import usrportal.repo.UserRepository;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInitializer implements CommandLineRunner {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public void run(String... strings) throws Exception {
    this.userRepository.deleteAll();
    User u1 = new User();
    u1.setUsername("example@example.com");
    u1.setPassword(passwordEncoder.encode("password"));
    u1.setIsEnabled(true);
    u1.setIsCredentialsNonExpired(true);
    u1.setIsAccountNonExpired(true);
    u1.setIsAccountNonLocked(true);
    u1.setRoles(Arrays.asList("USER"));
    userRepository.save(u1);
  }

}
