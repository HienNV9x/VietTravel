package com.viettravelbk.service.user;

import com.viettravelbk.service.user.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettravelbk.model.User;
import com.viettravelbk.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)								//Chỉ được đọc dữ liệu từ CSDL, ko được phép thay đổi
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
        	throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }
}
