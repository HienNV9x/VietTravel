package com.viettravelbk.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
