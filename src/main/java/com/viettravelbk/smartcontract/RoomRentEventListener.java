package com.viettravelbk.smartcontract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomRentEventListener {
    private final Twelve_SmartContract contract;

    @Autowired
    public RoomRentEventListener(Twelve_SmartContract contract) {
        this.contract = contract;
        listenToEvents();
    }

    private void listenToEvents() {
        listenToRoomRentResetEvent();
        listenToFirstRoomRentPaidEvent();
        listenToNotPaidYetEvent();
        listenToSecondRoomRentPaidEvent();
    }

    // Lắng nghe sự kiện RoomRentReset
    public void listenToRoomRentResetEvent() {
        contract.roomRentResetEventFlowable().subscribe(event -> {
            sendNotificationToUser("Giao dịch thuê phòng đã hủy");
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện: " + error.getMessage()));
    }

    // Lắng nghe sự kiện FirstRoomRentPaid
    private void listenToFirstRoomRentPaidEvent() {
        contract.firstRoomRentPaidEventFlowable().subscribe(event -> {
            sendNotificationToUser("Bạn đã thanh toán đủ 20% tiền thuê phòng");
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện FirstRoomRentPaid: " + error.getMessage()));
    }

    // Lắng nghe sự kiện NotPaidYet
    private void listenToNotPaidYetEvent() {
        contract.notPaidYetEventFlowable().subscribe(event -> {
            sendNotificationToUser("Bạn chưa thanh toán đủ 100% tiền thuê phòng. Giao dịch đặt phòng đã hủy");
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện NotPaidYet: " + error.getMessage()));
    }

    // Lắng nghe sự kiện SecondRoomRentPaid
    private void listenToSecondRoomRentPaidEvent() {
        contract.secondRoomRentPaidEventFlowable().subscribe(event -> {
            sendNotificationToUser("Bạn đã thanh toán đủ 100% tiền thuê phòng thành công");
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện SecondRoomRentPaid: " + error.getMessage()));
    }

    private void sendNotificationToUser(String message) {
        System.out.println(message);
    }
}
