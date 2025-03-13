package com.viettravelbk.service.user_email_crypto;

import com.viettravelbk.model.UserEmailCrypto;
import com.viettravelbk.repository.UserEmailCryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEmailCryptoServiceImpl implements IUserEmailCryptoService{
    @Autowired
    private UserEmailCryptoRepository userEmailCryptoRepository;

    @Override
    public UserEmailCrypto save(UserEmailCrypto userEmailCrypto) {
        return userEmailCryptoRepository.save(userEmailCrypto);
    }
}
