package usrportal.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import usrportal.email.EmailService;
import usrportal.repo.User;
import usrportal.repo.UserRepository;
import usrportal.utils.TokenUtils;

@RestController
@RequestMapping("/user")
public class UserController {

  @Value("${protocol}")
  private String protocol;

  @Value("${system.hostname}")
  private String hostname;

  @Value("${server.port}")
  private String port;

  @Autowired
  UserRepository repository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MessageSource i18n;

  Messages m = new Messages();

  TokenUtils tokenUtils = new TokenUtils();

  @Autowired
  TemplateEngine appTemplateEngine;

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  @RequestMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      method = RequestMethod.POST)
  public ResponseEntity<?> createUser(@RequestParam(value = "email") String emailAddress,
      @RequestParam(value = "password") String password) {
    log.info("UserController /create?email={}&password={}", emailAddress, password);
    Locale locale = LocaleContextHolder.getLocale();
    String successMsg = i18n.getMessage(m.REGISTRATION_SUCCESS, null, locale);
    String generalErrorMsg = i18n.getMessage(m.REGISTRATION_GENERAL_ERROR, null, locale);
    String activationEmailSubject = i18n.getMessage(m.ACTIVATION_EMAIL_SUBJECT, null, locale);

    List<User> user = repository.findByUsername(emailAddress);
    if (!user.isEmpty()) {
      return new ResponseEntity<>(Collections.singletonMap("message", generalErrorMsg),
          HttpStatus.CONFLICT);
    }
    User newUser = new User();
    newUser.setUsername(emailAddress);
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setIsEnabled(false);
    newUser.setIsCredentialsNonExpired(true);
    newUser.setIsAccountNonExpired(true);
    newUser.setIsAccountNonLocked(true);
    String secretToken = tokenUtils.generateSecretToken();
    newUser.setRegActivationToken(secretToken);
    newUser.setRoles(Arrays.asList("USER"));
    repository.save(newUser);

    Context context = new Context(locale);
    context.setVariable("url", protocol + "://" + hostname + ":" + port + "/user/activate?email="
        + emailAddress + "&token=" + secretToken);
    String emailMessage = appTemplateEngine.process("text/email-account-activation", context);
    emailService.sendMimeMail("registrationportal@example.com", emailAddress,
        activationEmailSubject, emailMessage);

    return new ResponseEntity<>(Collections.singletonMap("message", successMsg),
        HttpStatus.CREATED);
  }

  @RequestMapping(value = "/activate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      method = RequestMethod.GET)
  public ResponseEntity<?> activateRegistration(@RequestParam(value = "email") String emailAddress,
      @RequestParam(value = "token") String token) {
    Locale locale = LocaleContextHolder.getLocale();
    String successMsg = i18n.getMessage(m.ACTIVATION_SUCCESS, null, locale);
    String generalErrorMsg = i18n.getMessage(m.ACTIVATION_GENERAL_ERROR, null, locale);

    List<User> user = repository.findByUsername(emailAddress);
    if (user.isEmpty()) {
      log.info("UserController /activate empty resultset");
      return new ResponseEntity<>(Collections.singletonMap("message", generalErrorMsg),
          HttpStatus.NOT_FOUND);
    }
    User updateUser = user.get(0);
    if (!updateUser.getRegActivationToken().equals(token)) {
      log.info("UserController /activate token invalid");
      log.info("UserController /activate updateUser.getRegActivationToken()={}",
          updateUser.getRegActivationToken());
      log.info("UserController /activate token={}", token);
      return new ResponseEntity<>(Collections.singletonMap("message", generalErrorMsg),
          HttpStatus.NOT_FOUND);
    } else {
      log.info("UserController /activate?email={}&token={}", emailAddress, token);
      updateUser.setIsEnabled(true);
      repository.save(updateUser);
    }
    return new ResponseEntity<>(Collections.singletonMap("message", successMsg), HttpStatus.OK);
  }
}
