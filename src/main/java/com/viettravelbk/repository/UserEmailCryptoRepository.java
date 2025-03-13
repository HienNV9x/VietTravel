package com.viettravelbk.repository;

import com.viettravelbk.model.UserEmailCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserEmailCryptoRepository extends JpaRepository<UserEmailCrypto, Long> {
    @Query(value = "SELECT * FROM user_email_crypto WHERE address_account = ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserEmailCrypto findUserEmailCrypto(String addressAccount);
}
