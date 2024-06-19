package com.viettravelbk.service.oauth2;

import java.util.ArrayList;									
import java.util.Collections;
import java.util.List;										

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettravelbk.model.Role;							
import com.viettravelbk.model.User_OAuth2;
import com.viettravelbk.repository.RoleRepository;			
import com.viettravelbk.repository.UserRepository_OAuth2;

@Service
public class UserService_OAuth2 {
	@Autowired
	private UserRepository_OAuth2 repo;
	
    @Autowired
    private RoleRepository roleRepository;
	
    public void processOAuthPostLogin(String email, String username) {
        User_OAuth2 existUser = repo.getUser_OAuth2ByUsername(email);		//Lấy user đã tồn tại 
        if (existUser == null) {
        	Role userRoleOAuth2 = roleRepository.findByName("user"); 		//Lấy role của user trong database
            User_OAuth2 newUser = new User_OAuth2();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setRoles(Collections.singletonList(userRoleOAuth2));	//Cập nhật role cho user mới
            repo.save(newUser);
        } else {
            existUser.setUsername(username); 								//Cập nhật tên nếu người dùng đã tồn tại
            repo.save(existUser);
        }
    }
}
