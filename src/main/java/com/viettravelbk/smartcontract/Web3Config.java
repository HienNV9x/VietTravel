package com.viettravelbk.smartcontract;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import java.math.BigInteger;

@Configuration
public class Web3Config {
    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService("https://sepolia.infura.io/v3/"));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create("");
    }

    @Bean
    public TransactionManager web3TransactionManager(Web3j web3j, Credentials credentials) {
        return new RawTransactionManager(web3j, credentials, ); // Sepolia Chain ID
    }

    @Bean
    public ContractGasProvider gasProvider() {
        return new StaticGasProvider(
                Convert.toWei("50", Convert.Unit.GWEI).toBigInteger(),
                BigInteger.valueOf(6000000)
        );
    }

    @Bean
    public Fifteen_SmartContract twentyFivePaymentBookingRoom(Web3j web3j, TransactionManager web3TransactionManager, ContractGasProvider gasProvider) {
        String contractAddress = "";
        return Fifteen_SmartContract.load(contractAddress, web3j, web3TransactionManager, gasProvider);
    }
}
