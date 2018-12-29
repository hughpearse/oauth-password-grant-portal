package usrportal.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import usrportal.repo.User;
import usrportal.repo.UserRepository;
import usrportal.utils.EmailService;
import usrportal.utils.TokenUtils;

@RestController
@RequestMapping("/user")
public class UserController {
	
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
	
	TokenUtils tokenUtils = new TokenUtils();
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value="/create", produces=MediaType.APPLICATION_JSON_UTF8_VALUE, method=RequestMethod.POST)
    public ResponseEntity<?> createUser(
    		@RequestParam(value="email") String emailAddress,
    		@RequestParam(value="password") String password
    		){
		log.info("UserController /create?email={}&password={}", emailAddress, password);
    	List<User> user = repository.findByUsername(emailAddress);
    	if(!user.isEmpty()) {
    		return new ResponseEntity<>("{\"message\":\"Registration failed\"}", HttpStatus.CONFLICT);
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
    	emailService.sendMail("registrationportal@example.com", emailAddress,"Registration","http://"+hostname+":"+port+"/user/activate?email="+emailAddress+"&token="+secretToken);
    	return new ResponseEntity<>("{\"message\":\"Registration success, please check your email to activate your account.\"}", HttpStatus.CREATED);
    }
	
	@RequestMapping("/query")
    public ResponseEntity<?> createUser(@RequestParam(value="email") String emailAddress){
    	List<User> user = repository.findByUsername(emailAddress);
    	if(user.isEmpty()) {
    		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    	}
    	return new ResponseEntity<>(user.get(0), HttpStatus.OK);
    }
    
	@RequestMapping("/activate")
    public ResponseEntity<?> activateRegistration(
    		@RequestParam(value="email") String emailAddress,
    		@RequestParam(value="token") String token){
    	List<User> user = repository.findByUsername(emailAddress);
    	if(user.isEmpty()) {
    		log.info("UserController /activate empty resultset");
    		return new ResponseEntity<>("{\"message\":\"Activation Failed\"}", HttpStatus.NOT_FOUND);
    	}
    	User updateUser = user.get(0);
    	if(!updateUser.getRegActivationToken().equals(token)) {
    		log.info("UserController /activate token invalid");
    		log.info("UserController /activate updateUser.getRegActivationToken()={}", updateUser.getRegActivationToken());
    		log.info("UserController /activate token={}", token);
    		return new ResponseEntity<>("{\"message\":\"Activation Failed\"}", HttpStatus.NOT_FOUND);
    	} else {
    		log.info("UserController /activate?email={}&token={}", emailAddress, token);
    		updateUser.setIsEnabled(true);
    		repository.save(updateUser);
    	}
    	return new ResponseEntity<>("{\"message\":\"Account activated\"}", HttpStatus.OK);
    }
	
	@RequestMapping("/list")
    public ResponseEntity<?> listUsers(
    		@RequestParam(value="page", required=false) Optional<Integer> pageOpt,
    		@RequestParam(value="limit", required=false) Optional<Integer> limitOpt,
    		@RequestParam(value="sort", required=false) Optional<String> sortOpt){
		log.info("processing /list");
		Integer page = pageOpt.orElse(0);
    	Integer limit = limitOpt.orElse(10);
    	String sortStr = sortOpt.orElse("DESC");
    	Sort sort = new Sort(Sort.Direction.fromString(sortStr), new String[]{"id"});
    	Pageable pageable = PageRequest.of(page, limit, sort);
		return new ResponseEntity<>(repository.findAll(pageable), HttpStatus.OK);
	}
}
