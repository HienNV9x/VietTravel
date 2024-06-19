package com.viettravelbk.service;

import com.viettravelbk.model.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
    Long getUserIdByEmail(String email);				//Lấy email cho userId đăng nhập bằng OAuth2 xác thực userId
}
