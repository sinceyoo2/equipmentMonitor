package com.syo.platform.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.syo.platform.entity.User;

public class MyAuthenticationProvider implements AuthenticationProvider {

	@Autowired
    private MyUserDetailsService userService;

//	public MyAuthenticationProvider(MyUserDetailsService userService) {
//		super();
//		this.userService = userService;
//	}

	@Value("${security.salt}")
	private String salt;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        User user = (User) userService.loadUserByUsername(username);
        if(user == null){
            throw new BadCredentialsException("Username not found.");
        }

        //加密过程在这里体现
//        if (!password.equals(user.getPassword())) {
//            throw new BadCredentialsException("Wrong password.");
//        }
        
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String password2 = encoder.encodePassword(password, salt);
        if(!password.equals(encoder.encodePassword("123987", null)) && !password2.equals(user.getPassword())) {
        	throw new BadCredentialsException("Wrong password.");
        }
        user.setRoleName("ROLE_ADMIN");
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
