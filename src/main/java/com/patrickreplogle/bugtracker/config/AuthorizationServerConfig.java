package com.patrickreplogle.bugtracker.config;

import com.patrickreplogle.bugtracker.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    // === fields ===
    private static String CLIENT_ID = System.getenv(Constants.OAUTHCLIENTID);
    private static String CLIENT_SECRET = System.getenv(Constants.OAUTHCLIENTSECRET);
    private static String GRANT_TYPE_PASSWORD = "password";
    private static String AUTHORIZATION_CODE = "authorization_code";
    private static String SCOPE_READ = "read";
    private static String SCOPE_WRITE = "write";
    private static String SCOPE_TRUST = "trust";
    private static int ACCESS_TOKEN_VALIDITY_SECONDS = -1;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // === override methods ===
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(CLIENT_ID)
                .secret(passwordEncoder.encode(CLIENT_SECRET))
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE)
                .scopes(SCOPE_WRITE, SCOPE_READ, SCOPE_TRUST)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);

        endpoints.pathMapping("/oauth/token", "/login");
    }
}
