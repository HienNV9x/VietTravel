package com.viettravelbk.smartcontract;

import com.viettravelbk.email.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RoomRentEventListener {
    private final Twelve_SmartContract contract;
    private final EmailConfig emailConfig;

    @Autowired
    public RoomRentEventListener(Twelve_SmartContract contract, EmailConfig emailConfig) {
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

    // Lắng nghe sự kiện RoomRentReset
    /*public void listenToRoomRentResetEvent() {
        contract.roomRentResetEventFlowable().subscribe(event -> {
            sendNotificationToUser("Không thanh toán đủ tiền cọc thuê phòng. Giao dịch thuê phòng đã hủy");
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện: " + error.getMessage()));
    }*/
    public void listenToRoomRentResetEvent() {
        contract.roomRentResetEventFlowable().subscribe(event -> {
            try {
                System.out.println("Hello1");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("authentication1: " + authentication);
                String message = "Không thanh toán đủ tiền cọc thuê phòng. Giao dịch thuê phòng đã hủy";
                emailConfig.sendCryptoEmailEvent(message);
                System.out.println("Hello2");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }, error -> System.err.println("Lỗi khi lắng nghe sự kiện: " + error.getMessage()));
    }

    // Lắng nghe sự kiện FirstRoomRentPaid
    private void listenToFirstRoomRentPaidEvent() {
        contract.firstRoomRentPaidEventFlowable().subscribe(event -> {
            sendNotificationToUser("Bạn đã thanh toán đủ 20% tiền cọc thuê phòng");
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

    /*public void listenToRoomRentResetEvent() {
        contract.roomRentResetEventFlowable().subscribe(event -> {
            try {
                String message = "Không thanh toán đủ tiền cọc thuê phòng. Giao dịch thuê phòng đã hủy";
                emailConfig.sendCryptoEmail(message);
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }
        });
    }
    private void listenToFirstRoomRentPaidEvent() {
        contract.firstRoomRentPaidEventFlowable().subscribe(event -> {
            try {
                String message = "Bạn đã thanh toán đủ 20% tiền cọc thuê phòng";
                emailConfig.sendCryptoEmail(message);
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }
        });
    }
    private void listenToNotPaidYetEvent() {
        contract.notPaidYetEventFlowable().subscribe(event -> {
            try {
                String message = "Bạn chưa thanh toán đủ 100% tiền thuê phòng. Giao dịch đặt phòng đã hủy";
                emailConfig.sendCryptoEmail(message);
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }
        });
    }
    private void listenToSecondRoomRentPaidEvent() {
        contract.secondRoomRentPaidEventFlowable().subscribe(event -> {
            try {
                String message = "Bạn đã thanh toán đủ 100% tiền thuê phòng thành công";
                emailConfig.sendCryptoEmail(message);
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }
        });
    }*/

    /*private String sendNotificationToUser(String message) {
        try {
            emailConfig.sendCryptoEmail(message);
            return message;
        }catch (Exception e) {
            e.printStackTrace();
            return "Error send email: " + e.getMessage();
        }
    }*/
}
