package co.istad.mbanking.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity //disable default spring security configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

//

    // create provider for access user and password from database to spring security
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;

    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers("api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("SCOPE_user:write")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority("SCOPE_user:write")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAuthority("SCOPE_user:read")
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts/**").hasAuthority("SCOPE_account:write")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/accounts/**").hasAuthority("SCOPE_account:write")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/accounts/**").hasAuthority("SCOPE_account:write")
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts/**").hasAnyAuthority("SCOPE_ROLE_ADMIN", "SCOPE_account:read")
                        .anyRequest()
                        .authenticated()
                );

        // enable spring security configuration or enable form login basic username and password for authentication
//        httpSecurity.httpBasic(Customizer.withDefaults());

        // enable jwt security
        httpSecurity.oauth2ResourceServer(jwt -> jwt
                .jwt(Customizer.withDefaults()));

        // disable csrf for submit form because we develop api
        httpSecurity.csrf(token -> token.disable());

        // change from statefull to stateless because api use stateless
        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Primary
    @Bean("keyPair")
    public KeyPair keyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    @Primary
    @Bean("rsaKey")
    public RSAKey rsaKey(@Qualifier("keyPair") KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair().getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Primary
    @Bean("jwkSource")
    JWKSource<SecurityContext> jwkSource(@Qualifier("rsaKey") RSAKey rsaKey) {

        JWKSet jwkSet = new JWKSet(rsaKey);

        return ((jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet));
    }

    // issue access token or get access token
    @Primary
    @Bean("jwtEncoder")
    JwtEncoder jwtEncoder(@Qualifier("jwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // when client submit token from header to get the protected resource
    @Primary
    @Bean("jwtDecoder")
    JwtDecoder jwtDecoder(@Qualifier("rsaKey") RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }


    @Bean("refreshKeyPair")
    public KeyPair refreshKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            return keyPairGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    @Bean("refreshRsaKey")
    public RSAKey refreshRsaKey(@Qualifier("refreshKeyPair") KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair().getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean("refreshJwkSource")
    JWKSource<SecurityContext> refreshJwkSource(@Qualifier("refreshRsaKey") RSAKey rsaKey) {

        JWKSet jwkSet = new JWKSet(rsaKey);

        return ((jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet));
    }

    // issue access token or get access token
    @Bean("refreshJwtEncoder")
    JwtEncoder refreshJwtEncoder(@Qualifier("refreshJwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // when client submit token from header to get the protected resource
    @Primary
    @Bean("refreshJwtDecoder")
    JwtDecoder refreshJwtDecoder(@Qualifier("refreshRsaKey") RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }


//    @Bean
//    InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//
//        UserDetails userAdmin = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("admin"))
//                .roles("USER","ADMIN")
//                .build();
//
//        UserDetails userEditor = User.builder()
//                .username("staff")
//                .password(passwordEncoder.encode("staff"))
//                .roles("USER","STAFF")
//                .build();
//
//        inMemoryUserDetailsManager.createUser(userAdmin);
//        inMemoryUserDetailsManager.createUser(userEditor);
//
//        return inMemoryUserDetailsManager;
//    }
}
