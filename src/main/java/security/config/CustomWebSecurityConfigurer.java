package security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity//( debug = true )
@Order(1)
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
	        .antMatchers("/")
	        .antMatchers("/user/**")
	        .antMatchers("/register")
	        .antMatchers("/h2-console/**");
    }
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
