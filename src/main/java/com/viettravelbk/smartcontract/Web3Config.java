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
        return Web3j.build(new HttpService("https://sepolia.infura.io/v3/5c0d8d2e20e541988a45c073c4670fc7"));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create("847eaf3fe97840f59de2709d922bb4a7de24f9b4dacecce0bddae8d71b65bf67");
    }

    @Bean
    public TransactionManager web3TransactionManager(Web3j web3j, Credentials credentials) {
        return new RawTransactionManager(web3j, credentials, 11155111); // Sepolia Chain ID
    }

    @Bean
    public ContractGasProvider gasProvider() {
        return new StaticGasProvider(
                Convert.toWei("50", Convert.Unit.GWEI).toBigInteger(),
                BigInteger.valueOf(5000000)
        );
    }

    /*@Bean
    public TwentySevenPaymentBookingRoom fourteenthPaymentBookingRoom(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        String contractAddress = "0x3212074bf50639c15a3bbd65045845f9a0476876";
        return TwentySevenPaymentBookingRoom.load(contractAddress, web3j, credentials, gasProvider);
    }*/
    @Bean
    public Twelve_SmartContract twentyFivePaymentBookingRoom(Web3j web3j, TransactionManager web3TransactionManager, ContractGasProvider gasProvider) {
        String contractAddress = "0xb1382c94c03a5387b14a472a24321675e5f29ce7";
        return Twelve_SmartContract.load(contractAddress, web3j, web3TransactionManager, gasProvider);
    }
}
