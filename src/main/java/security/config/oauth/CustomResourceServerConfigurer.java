package security.config.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Resource Server: provides identity information after authentication.
 * 
 * @author hughpearse
 *
 */
@Configuration
@EnableResourceServer
public class CustomResourceServerConfigurer extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/user/**").permitAll()
        .antMatchers("/register").permitAll().antMatchers("/h2-console/**").permitAll()
        .antMatchers("/private/listusers").authenticated();
  }

}
