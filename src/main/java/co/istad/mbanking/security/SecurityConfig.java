package co.istad.mbanking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //disable default spring security configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

//

    // create provider for access user and password from database to spring security
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;

    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST,"api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"api/v1/users/**").hasAnyRole("ADMIN","STAFF")
                        .anyRequest()
                        .authenticated()
                );

        // enable spring security configuration or enable form login basic username and password for authentication
        httpSecurity.httpBasic(Customizer.withDefaults());

        // disable csrf for submit form because we develop api
        httpSecurity.csrf(token -> token.disable());

        // change from statefull to stateless because api use stateless
        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
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
