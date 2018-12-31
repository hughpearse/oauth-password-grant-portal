package usrportal.ui;

import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import usrportal.repo.User;
import usrportal.repo.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	TemplateEngine appTemplateEngine;
	
	@RequestMapping("/")
	public ResponseEntity<?> welcome() {
		Locale locale = LocaleContextHolder.getLocale();
		Context context = new Context(locale);
		String response = appTemplateEngine.process("html/home.html", context);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping("/register")
	public ResponseEntity<?> register() {
		Locale locale = LocaleContextHolder.getLocale();
		Context context = new Context(locale);
		String response = appTemplateEngine.process("html/register.html", context);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping("/private/listusers")
	public ResponseEntity<?> listUsers() {
		Locale locale = LocaleContextHolder.getLocale();
		Context context = new Context(locale);
		List<User> users = repository.findAll();
		context.setVariable("users", users);
		String response = appTemplateEngine.process("html/listusers.html", context);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
