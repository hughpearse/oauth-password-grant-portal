package usrportal.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import usrportal.repo.User;
import usrportal.repo.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	UserRepository repository;
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "home";
	}
	
	@RequestMapping("/register")
	public String register(Map<String, Object> model) {
		return "register";
	}
	
	@RequestMapping("/private/listusers")
	public ModelAndView listUsers(Map<String, Object> model) {
		List<User> cars = repository.findAll();
		Map<String, Object> params = new HashMap<>();
		params.put("users", cars);
		return new ModelAndView("listusers", params);
	}

}
