package com.viettravelbk.smartcontract;

import com.viettravelbk.email.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UpdateRoomRentService {
    BigInteger base = BigInteger.valueOf(10);
    BigInteger exponentation = base.pow(18);
    private final Fifteen_SmartContract contract;
    private final EmailConfig emailConfig;
    @Autowired
    public UpdateRoomRentService(Fifteen_SmartContract contract, EmailConfig emailConfig) {
        this.contract = contract;
        this.emailConfig = emailConfig;
    }

    public String updateTotalRoomRent(RoomRentCreateRequest createRequest) {
        try {
            // Chuyển đổi từ ETH sang Wei (để gửi vào Smart Contract)
            BigInteger newTotalRoomRent = Convert.toWei(createRequest.getRoomRent(), Convert.Unit.ETHER).toBigInteger();

            // Chuyển đổi ngược lại từ Wei sang ETH để hiển thị
            BigDecimal amountInEther = Convert.fromWei(newTotalRoomRent.toString(), Convert.Unit.ETHER);

            // Gửi giao dịch tới Smart Contract
            //TransactionReceipt receipt = contract.updateTotalRoomRent(newTotalRoomRent).send();

            // Gửi giao dịch tới Smart Contract với địa chỉ người thuê
            TransactionReceipt receipt = contract.updateTotalRoomRent(newTotalRoomRent, createRequest.getRenterAddress()).send();

            String transactionMessage = "Địa chỉ tài khoản: " + createRequest.getRenterAddress() + " cập nhật thành công số tiền thuê phòng " + amountInEther + " ETH Sepolia thông qua SmartContract trên mạng Ethereum Testnet Sepolia." +
            "<br>Hash giao dịch: " + receipt.getTransactionHash();
            emailConfig.sendCryptoEmail(transactionMessage, createRequest.getRenterAddress());
            return transactionMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi cập nhật: " + e.getMessage();
        }
    }
}
