package com.viettravelbk.service.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.viettravelbk.model.User_OAuth2;
import com.viettravelbk.repository.UserRepository_OAuth2;

public class UserDetailsServiceImpl_OAuth2 implements UserDetailsService{
	@Autowired
	private UserRepository_OAuth2 userRepository_OAuth2;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User_OAuth2 user = userRepository_OAuth2.getUser_OAuth2ByUsername(username);
		return new MyUserDetails_OAuth2(user);
	}
}
