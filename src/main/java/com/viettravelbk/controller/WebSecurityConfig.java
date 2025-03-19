package com.viettravelbk.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.viettravelbk.service.user.UserDetailsServiceImpl;
import com.viettravelbk.service.oauth2.CustomOAuth2User;
import com.viettravelbk.service.oauth2.CustomOAuth2UserService;
import com.viettravelbk.service.oauth2.UserService_OAuth2;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }
    
	@Autowired
	private CustomOAuth2UserService oauthUserService;
	@Autowired
	private UserService_OAuth2 userService;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()			//csrf() là mặc định của springboot để chặn các yêu cầu thay đổi db - disable() để mở tính năng thay đổi dữ liệu trong db
            .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img_more/**", "/fonts/**", "/fonts_awesome/**", "/vendor/**", "/uploads/**", 
                		"/viettravel/registration", "/", "/viettravel/more",
                		"/viettravel/hagiang", "/viettravel/hanoi", "/viettravel/halong", "/viettravel/ninhbinh",
                		"/viettravel/danang", "/viettravel/hoian", "/viettravel/dalat", "/viettravel/tphcm",
                		"/viettravel/phuquoc", "/viettravel/camau", "/submitOrder", "/vnpay-payment", "/viettravel/admin", 
                		"/viettravel/user", "/oauth/**", "/move", "/room/**", "/roomPoint/**", "/cuisine/**", "/intertainment/**",
                		"/detail", "/comment/**", "/cart", "/search_room", "/viettravel/search_room", "/api/userId")
				.permitAll()
                .anyRequest().authenticated()
                //.antMatchers("/**").permitAll()
			.and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
				.successHandler(loginSuccessHandler())
                //.defaultSuccessUrl("/")
            .and()
            .oauth2Login()
    			.loginPage("/login")
    			.userInfoEndpoint()
    				.userService(oauthUserService)
    			.and()
				.successHandler(loginSuccessHandler())
    			/*.successHandler(new AuthenticationSuccessHandler() {
    				@Override
    				public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException, ServletException {
    					CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();   					
    					userService.processOAuthPostLogin(oauthUser.getEmail(), oauthUser.getName()); // Chỉ gọi một phương thức với cả email và tên				
    					response.sendRedirect("/");
    				}
    			})*/
            .and()
            .logout()
            	.logoutSuccessUrl("/")
                .permitAll();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
												Authentication authentication) throws IOException, ServletException {
				// Kiểm tra nếu đăng nhập bằng OAuth2
				if (authentication.getPrincipal() instanceof CustomOAuth2User) {
					CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
					userService.processOAuthPostLogin(oauthUser.getEmail(), oauthUser.getName());
				}

				// Lấy URL trước khi vào Login từ session
				String targetUrl = (String) request.getSession().getAttribute("prevPage");

				if (targetUrl != null && !targetUrl.contains("/login")) {
					// Xóa session sau khi lấy URL
					request.getSession().removeAttribute("prevPage");
					response.sendRedirect(targetUrl);
				} else {
					response.sendRedirect("/");
				}
			}
		};
	}
}
