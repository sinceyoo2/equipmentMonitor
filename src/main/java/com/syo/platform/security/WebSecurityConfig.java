package com.syo.platform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.syo.platform.sysLog.login.LoginSuccessHandler;
import com.syo.platform.sysLog.login.LogoutActionHandler;
import com.syo.platform.sysLog.login.LogoutSuccessHandler;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	UserDetailsService myUserDetailsService() {
		return new MyUserDetailsService();
	}
	
	@Bean
    MyAuthenticationProvider authenticationProvider() {//自定义验证
		return new MyAuthenticationProvider();
	}
	
	@Bean
	LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler();
	}
	
//	@Bean
//	LogoutSuccessHandler logoutSuccessHandler() {
//		return new LogoutSuccessHandler();
//	}
	
	@Bean
	LogoutHandler logoutHandler() {
		return new LogoutActionHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService());
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
				.authorizeRequests()
				.antMatchers("/assets/**").permitAll()
				.antMatchers("/api/**").permitAll()
				.anyRequest().authenticated()
				.and().formLogin().loginPage("/login")
				.defaultSuccessUrl("/index").failureUrl("/login?error").permitAll()
				.successHandler(loginSuccessHandler())
				.and()
				.logout()
				.addLogoutHandler(logoutHandler())
//				.logoutSuccessHandler(logoutSuccessHandler())
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				//.logoutUrl("/logout")
				.logoutSuccessUrl("/login")
				.permitAll();
	}
	
}
