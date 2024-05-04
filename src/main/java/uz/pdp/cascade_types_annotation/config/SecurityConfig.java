package uz.pdp.cascade_types_annotation.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.cascade_types_annotation.security.JwtFilter;
import uz.pdp.cascade_types_annotation.service.AuthService;

import java.util.Properties;


  @Configuration
  @EnableWebSecurity
  public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  JwtFilter  jwtFilter;

  @Autowired
  AuthService authService;


      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
      }

      @Bean
      @Override
      public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
      }

      @Bean
      PasswordEncoder passwordEncoder(){
          return new BCryptPasswordEncoder();
      };

      @Override
      protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(
                          //"/api/auth/**"
                        "/api/auth/register/companyDirector",
                        "/api/auth/verifyEmail/**",
                        "/api/auth/login/**"
              ) .permitAll()
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      }

      @Bean
      public JavaMailSender javaMailSender() {
          try {

              JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

                  mailSender.setHost("smtp.gmail.com");
                  mailSender.setPort(587);
                  mailSender.setUsername("aslon.dinov@gmail.com");
                  mailSender.setPassword("ughwfjhrmtjj bpyfmd");

                  Properties properties = mailSender.getJavaMailProperties();
                      properties.put("mail.transport.protocol", "smtp");
                      properties.put("mail.smtp.auth", "true");
                      properties.put("mail.smtp.starttls.enable", "true");
                      properties.put("mail.debug", "true");
                      return mailSender;

          } catch (Exception e) {
              return null;
          }

      }

  }
