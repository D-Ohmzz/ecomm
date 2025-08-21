package com.ecomm.ecomm.security.config;

import com.ecomm.ecomm.model.AppRole;
import com.ecomm.ecomm.model.Role;
import com.ecomm.ecomm.model.User;
import com.ecomm.ecomm.repository.RoleRepository;
import com.ecomm.ecomm.repository.UserRepository;
import com.ecomm.ecomm.security.jwt.AuthEntryPointJwt;
import com.ecomm.ecomm.security.jwt.AuthTokenFilter;
import com.ecomm.ecomm.security.jwt.JwtUtils;
import com.ecomm.ecomm.security.services.UserDetailsServiceImplementation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {
    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
    private final AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(UserDetailsServiceImplementation userDetailsServiceImplementation, AuthEntryPointJwt unauthorizedHandler){
        this.unauthorizedHandler=unauthorizedHandler;
        this.userDetailsServiceImplementation=userDetailsServiceImplementation;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter(jwtUtils(), userDetailsService());
    }

    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceImplementation);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests)->
                requests.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        //.requestMatchers("/api/public/**").permitAll()
                        //.requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest()
                        .authenticated());
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.headers(headers->headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web->web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/cnofiguration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            //Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(()->{
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });
            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(()->{
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });
            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(()->{
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles=Set.of(userRole);
            Set<Role> sellerRoles=Set.of(sellerRole);
            Set<Role> adminRoles=Set.of(adminRole);

            userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(()->{
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });
            sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(()->{
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });
            adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(()->{
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            //Create users if not already present
            if(!userRepository.existsByUsername("user1")){
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("user1"));
                userRepository.save(user1);
            }
            if(!userRepository.existsByUsername("admin1")){
                User admin1 = new User("admin1", "admin1@example.com", passwordEncoder.encode("admin1"));
                userRepository.save(admin1);
            }
            if(!userRepository.existsByUsername("seller1")){
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("seller1"));
                userRepository.save(seller1);
            }

            //Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user->{
                user.setRoles(userRoles);
                userRepository.save(user);
            });
            userRepository.findByUsername("seller1").ifPresent(seller->{
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });
            userRepository.findByUsername("admin1").ifPresent(admin->{
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }


}
