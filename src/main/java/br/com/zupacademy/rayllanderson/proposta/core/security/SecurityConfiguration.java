package br.com.zupacademy.rayllanderson.proposta.core.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize ->
                        authorize.antMatchers(HttpMethod.POST,"/proposals").hasAuthority("SCOPE_proposal:write")
                        .antMatchers(HttpMethod.GET,"/proposals/**").hasAuthority("SCOPE_proposal:read")
                        .antMatchers("/cards/**").hasAuthority("SCOPE_card:write")
                        .antMatchers(HttpMethod.GET, "/actuator/prometheus").permitAll()
                        .antMatchers(HttpMethod.GET, "/actuator/**").hasAuthority("SCOPE_actuator:read")
                        .anyRequest().authenticated()
                )
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
