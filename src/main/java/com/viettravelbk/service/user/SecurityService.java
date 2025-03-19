package com.viettravelbk.service.user;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
