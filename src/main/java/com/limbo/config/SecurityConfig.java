package com.limbo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limbo.common.CommonResult;
import com.limbo.modal.User;
import com.limbo.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * spring security cnfig
 *
 * @author limbo
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsServiceImpl userDetailsService;
    private final CustomUrlDecisionManager customUrlDecisionManager;

    @Autowired
    public SecurityConfig(CustomUserDetailsServiceImpl userDetailsService, CustomUrlDecisionManager customUrlDecisionManager) {
        this.userDetailsService = userDetailsService;
        this.customUrlDecisionManager = customUrlDecisionManager;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }


    /**
     * url ????????????
     *
     * @param http HttpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .logout()
                .logoutSuccessHandler((req, resp, authentication) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new ObjectMapper().writeValueAsString(CommonResult.success("????????????!")));
                            out.flush();
                            out.close();
                        }
                )
                .permitAll()
                .and()
                .csrf().disable().exceptionHandling()
                //?????????????????????????????????????????????????????????
                .authenticationEntryPoint((req, resp, authException) -> {
                            resp.setContentType("application/json;charset=utf-8");
                            resp.setStatus(401);
                            PrintWriter out = resp.getWriter();
                            CommonResult<?> result = CommonResult.failed("????????????!");
                            if (authException instanceof InsufficientAuthenticationException) {
                                result.setMessage("?????????????????????????????????!");
                            }
                            out.write(new ObjectMapper().writeValueAsString(result));
                            out.flush();
                            out.close();
                        }
                );
        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new ConcurrentSessionFilter(sessionRegistry(), event -> {
            HttpServletResponse resp = event.getResponse();
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(401);
            PrintWriter out = resp.getWriter();
            out.write(new ObjectMapper().writeValueAsString(CommonResult.unauthorized()));
            out.flush();
            out.close();
        }), ConcurrentSessionFilter.class);
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    User user = (User) authentication.getPrincipal();
                    user.setPassword(null);
                    String s = new ObjectMapper().writeValueAsString(CommonResult.success(user, "????????????!"));
                    out.write(s);
                    out.flush();
                    out.close();
                }
        );
        customAuthenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    CommonResult<?> commonResult = CommonResult.failed(exception.getMessage());
                    if (exception instanceof LockedException) {
                        commonResult.setMessage("????????????????????????????????????!");
                    } else if (exception instanceof CredentialsExpiredException) {
                        commonResult.setMessage("?????????????????????????????????!");
                    } else if (exception instanceof AccountExpiredException) {
                        commonResult.setMessage("?????????????????????????????????!");
                    } else if (exception instanceof DisabledException) {
                        commonResult.setMessage("????????????????????????????????????!");
                    } else if (exception instanceof BadCredentialsException) {
                        commonResult.setMessage("???????????????????????????????????????????????????!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(commonResult));
                    out.flush();
                    out.close();
                }
        );
        customAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        customAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy);
        return customAuthenticationFilter;
    }
}
