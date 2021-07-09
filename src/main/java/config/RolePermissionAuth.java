package config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RolePermissionAuth {
    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        protected void configure(AuthenticationManagerBuilder auth) throws Exception{
            auth
                    .inMemoryAuthentication()
                    .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
                    .and()
                    .withUser("owner").password(passwordEncoder().encode("owner")).roles("OWNER")
                    .and()
                    .withUser("user").password(passwordEncoder().encode("user")).roles("USER");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/facebookpost/**").hasAnyRole("USER", "OWNER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/facebookpost").hasAnyRole("ADMIN", "OWNER")
                    .antMatchers("/api/facebookpost/**").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic();
        }

        @Bean
        PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
    }
}
