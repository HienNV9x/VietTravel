package com.viettravelbk.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_email_crypto")
public class UserEmailCrypto {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "address_account")
    private String addressAccount;
}
