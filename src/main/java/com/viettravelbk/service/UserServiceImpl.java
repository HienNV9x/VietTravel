package com.viettravelbk.service;

import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.viettravelbk.model.User;
import com.viettravelbk.model.User_OAuth2;
import com.viettravelbk.repository.RoleRepository;
import com.viettravelbk.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));			//Mã hóa mật khẩu    
        com.viettravelbk.model.Role userRole = roleRepository.findByName("user");   //Lấy vai trò "user" từ bảng Role   
        user.setRoles(Collections.singletonList(userRole));   						//Đặt vai trò cho người dùng    
        userRepository.save(user);													//Lưu người dùng vào cơ sở dữ liệu
    }
    
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public Long getUserIdByEmail(String email) {									//Lấy email cho User Login by OAuth2 xác thực userId
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getId();
        }
        return null;
    }
    
    @Override
    public boolean existsByUsername(String username) {  
        return userRepository.findByUsername(username) != null;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
