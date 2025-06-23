package com.viettravelbk.smartcontract;

import com.viettravelbk.email.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomRentEventListener {
    private final Fifteen_SmartContract contract;
    private final EmailConfig emailConfig;

    @Autowired
    public RoomRentEventListener(Fifteen_SmartContract contract, EmailConfig emailConfig) {
        this.contract = contract;
        this.emailConfig = emailConfig;
        listenToEvents();
    }

    private void listenToEvents() {
        listenToRoomRentResetEvent();
        listenToFirstRoomRentPaidEvent();
        listenToNotPaidYetEvent();
        listenToSecondRoomRentPaidEvent();
    }

    public void listenToRoomRentResetEvent() {
        contract.roomRentResetEventFlowable().subscribe(event -> {
            String payerAddress = event.payer;
            String message = "Địa chỉ tài khoản: " + payerAddress + " không thanh toán đủ tiền cọc thuê phòng. Giao dịch thuê phòng đã hủy";
            emailConfig.sendCryptoEmailEvent(message, payerAddress);
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện: " + error.getMessage()));
    }

    private void listenToFirstRoomRentPaidEvent() {
        contract.firstRoomRentPaidEventFlowable().subscribe(event -> {
            String payerAddress = event.payer;
            String message = "Địa chỉ tài khoản: " + payerAddress + " đã thanh toán đủ 20% tiền cọc thuê phòng";
            emailConfig.sendCryptoEmailEvent(message, payerAddress);
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện FirstRoomRentPaid: " + error.getMessage()));
    }

    private void listenToNotPaidYetEvent() {
        contract.notPaidYetEventFlowable().subscribe(event -> {
            String payerAddress = event.payer;
            String message = "Địa chỉ tài khoản: " + payerAddress + " chưa thanh toán đủ 100% tiền thuê phòng. Giao dịch đặt phòng đã hủy";
            emailConfig.sendCryptoEmailEvent(message, payerAddress);
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện NotPaidYet: " + error.getMessage()));
    }

    private void listenToSecondRoomRentPaidEvent() {
        contract.secondRoomRentPaidEventFlowable().subscribe(event -> {
            String payerAddress = event.payer;
            String message = "Địa chỉ tài khoản: " + payerAddress + " đã thanh toán đủ 100% tiền thuê phòng thành công";
            emailConfig.sendCryptoEmailEvent(message, payerAddress);
            emailConfig.sendHTMLEmailWithAttachmentCrypto(payerAddress);
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện SecondRoomRentPaid: " + error.getMessage()));
    }
}
