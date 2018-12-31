package security.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import usrportal.utils.PasswordUtils;

/**
 * Authorization Server: The OAuth2 server that presents the interface where the user approves or
 * denies the request. This issues an access token to the client. In smaller implementations, this
 * may be the same server as the resource server.
 * 
 * @author hughpearse
 *
 */
@Configuration
@EnableAuthorizationServer
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${security.oauth2.resource.id}")
  private String resourceId;

  @Value("${security.oauth2.client.client-id}")
  private String clientId;

  @Value("${security.oauth2.client.client-secret}")
  private String clientSecret;

  @Value("${token.access.validity-period}")
  private int accessTokenValiditySeconds;

  @Value("${token.refresh.validity-period}")
  private int refreshTokenValiditySeconds;

  @Value("${security.oauth2.redirecturi}")
  private String redirectUri;

  /**
   * Configure the non-security features of the Authorization Server endpoints, like token store,
   * token customizations, user approvals and grant types.
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()")
        .passwordEncoder(passwordEncoder);
  }

  /**
   * Configure the ClientDetailsService, declaring individual clients and their properties.
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory().withClient(clientId).authorizedGrantTypes("password", "refresh_token")// local
                                                                                             // oauth2
        .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write", "trust")
        .resourceIds("oauth2-resource").accessTokenValiditySeconds(accessTokenValiditySeconds)
        .secret(clientSecret);
  }

  /**
   * Configure the security of the Authorization Server, which means in practical terms the
   * /oauth/token endpoint. Get secret code return signed JWT
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new PasswordUtils();
  }
}
