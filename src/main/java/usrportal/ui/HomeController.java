package usrportal.ui;

import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
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
  public ResponseEntity<?> listUsers(
      @RequestParam(value = "page", required = false) Optional<Integer> pageOpt,
      @RequestParam(value = "limit", required = false) Optional<Integer> limitOpt,
      @RequestParam(value = "sort", required = false) Optional<String> sortOpt) {
    Locale locale = LocaleContextHolder.getLocale();
    Context context = new Context(locale);
    Integer page = pageOpt.orElse(0);
    Integer limit = limitOpt.orElse(10);
    String sortStr = sortOpt.orElse("DESC");
    Sort sort = new Sort(Sort.Direction.fromString(sortStr), new String[] {"id"});
    Pageable pageable = PageRequest.of(page, limit, sort);
    context.setVariable("users", repository.findAll(pageable));
    String response = appTemplateEngine.process("html/listusers.html", context);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
