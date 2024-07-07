package com.viettravelbk.service;

import com.viettravelbk.model.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
    Long getUserIdByEmail(String email);				//Lấy email cho userId đăng nhập bằng OAuth2 xác thực userId
    boolean existsByUsername(String username);			//Kiểm tra sự tồn tại của username
    boolean existsByEmail(String email);				//Kiểm tra sự tồn tại của email
}
