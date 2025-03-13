package com.viettravelbk.email;

import java.io.File;
import java.util.Arrays;														
import java.util.Collections;													
import java.util.Comparator;													
import java.util.List;															

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.viettravelbk.model.UserEmailCrypto;
import com.viettravelbk.repository.UserEmailCryptoRepository;
import com.viettravelbk.service.user_email_crypto.UserEmailCryptoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;									
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;										
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viettravelbk.model.Revenue;											
import com.viettravelbk.model.User;
import com.viettravelbk.repository.RevenueRepository;								
import com.viettravelbk.repository.UserRepository;
import com.viettravelbk.service.oauth2.CustomOAuth2User;								


@RestController 															//Sử dụng @RestController để chỉ ra rằng class này sẽ trả về dữ liệu thô thay vì view
public class EmailConfig {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RevenueRepository revenueRepository;

    @Autowired
    private UserEmailCryptoRepository userEmailCryptoRepository;

    @Autowired
    private UserEmailCryptoServiceImpl userEmailCryptoServiceImpl;

    //Send Email via HTML
    @GetMapping("/send_html_email")
    public ResponseEntity<?> sendHTMLEmail() throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "";
        if (authentication != null) {										//Xác thực đối tượng có đang đăng nhập hay ko?
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                currentPrincipalName = ((UserDetails) principal).getUsername();
            } else if (principal instanceof CustomOAuth2User) {
                currentPrincipalName = ((CustomOAuth2User) principal).getName();
            } else {
                currentPrincipalName = principal.toString();
            }
        }

        User user = userRepository.findByUsername(currentPrincipalName);	//Lấy đối tượng đang đăng nhập
        if (user == null) {													//Handle the case where the user is not found.                        
            return ResponseEntity.notFound().build();						//Trường hợp không tìm thấy user, trả về status 404 Not Found
        }
         
        // Lấy thông tin revenue mới nhất được tạo bởi người dùng hiện tại
        List<Revenue> revenues = revenueRepository.findByCreatedByOrderByCreatedDateDesc(currentPrincipalName, PageRequest.of(0, 1));
        Revenue lastRevenue = revenues.isEmpty() ? null : revenues.get(0);
        if (lastRevenue == null) {
            return ResponseEntity.badRequest().body("No revenue data found for current user to send email");
        }

        String to = user.getEmail();										//Lấy thông tin email đối tượng đang đăng nhập
        String from = "viettravel2509@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("Your Information From VietTravel");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>")
                .append("<h2 style='color: #2E86C1;'>Hello ").append(user.getUsername()).append(",</h2>")
                .append("<p style='font-size: 16px;'>Thank you very much for using <strong>VietTravel</strong> products!</p>")
                .append("<p style='font-size: 16px;'>We confirm that you have registered to use the following services:</p>")
                .append("<hr style='border: 1px solid #ddd;'>");

        if (lastRevenue.getTitle_room() != null && !lastRevenue.getTitle_room().isEmpty()) {
            emailContent.append("<p style='font-size: 16px;'><strong>Room booked:</strong> ")
                    .append(lastRevenue.getTitle_room()).append("</p>");
        }
        if (lastRevenue.getCategory_room() != null && !lastRevenue.getCategory_room().isEmpty()) {
            emailContent.append("<p style='font-size: 16px;'><strong>Sites:</strong> ")
                    .append(lastRevenue.getCategory_room()).append("</p>");
        }
        if (lastRevenue.getCuisineLocal() != null && !lastRevenue.getCuisineLocal().isEmpty()) {
            emailContent.append("<p style='font-size: 16px;'><strong>Cuisine:</strong> ")
                    .append(lastRevenue.getCuisineLocal()).append("</p>");
        }
        if (lastRevenue.getIntertainmentLocal() != null && !lastRevenue.getIntertainmentLocal().isEmpty()) {
            emailContent.append("<p style='font-size: 16px;'><strong>Entertainment:</strong> ")
                    .append(lastRevenue.getIntertainmentLocal()).append("</p>");
        }
        if (lastRevenue.getMoveLocal() != null && !lastRevenue.getMoveLocal().isEmpty()) {
            emailContent.append("<p style='font-size: 16px;'><strong>Transport:</strong> ")
                    .append(lastRevenue.getMoveLocal()).append("</p>");
        }
        if (lastRevenue.getIncome() > 0) {
            emailContent.append("<p style='font-size: 16px;'><strong>The amount paid:</strong> ")
                    .append(lastRevenue.getIncome()).append(" $</p>");
        }
        emailContent.append("<p style='font-size: 16px;'>Please select the appropriate payment method.</p>")
                .append("<p style='font-size: 16px;'>If you need immediate assistance, please call us at "
                        + "<a href='tel:+84966680989' style='color: #E74C3C; text-decoration: none; font-weight: bold;'>+84966.68.0989</a>.</p>")
                .append("<p style='font-size: 16px;'><strong>Best regards,</strong></p>")
                .append("<p style='font-size: 16px; color: #2E86C1;'><strong>VietTravel Support Team</strong></p>")
                .append("</div>");

        helper.setText(emailContent.toString(), true);
        mailSender.send(message);
        return ResponseEntity.ok().body("Email has been sent successfully!");	//Trả về status 200 OK khi gửi email thành công
    }

    //Send Email via PDF
	@GetMapping("/send_email_attachment")
	public ResponseEntity<?> sendHTMLEmailWithAttachment() throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "";
        if (authentication != null) {											//Xác thực đối tượng có đang đăng nhập hay ko?
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                currentPrincipalName = ((UserDetails) principal).getUsername();
            } else if (principal instanceof CustomOAuth2User) {
                currentPrincipalName = ((CustomOAuth2User) principal).getName();
            } else {
                currentPrincipalName = principal.toString();
            }
        }
        User user = userRepository.findByUsername(currentPrincipalName);		//Lấy đối tượng đang đăng nhập
        if (user == null) {														//Handle the case where the user is not found.                        
            return ResponseEntity.notFound().build();							//Trường hợp không tìm thấy user, trả về status 404 Not Found
        }
        
        //Danh sách các File PDF đã lưu
        String directoryPath = "C:\\Users\\ADMIN\\eclipse-workspace\\VIETTRAVELBK\\src\\main\\resources\\static\\FilePDF\\FileExportPDF";
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.startsWith("InvoiceVietTravel_" + user.getUsername()) && name.endsWith(".pdf"));
        if (files == null || files.length == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No invoice file found for user.");
        }
        
        //Sắp xếp lại các File để tìm kiếm File có thời gian muộn nhất
        File latestFile = Collections.max(Arrays.asList(files), Comparator.comparingLong(File::lastModified));
		
		String from = "viettravel2509@gmail.com";
		String to = user.getEmail();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setSubject("Your Invoice From VietTravel");
		helper.setFrom(from);
		helper.setTo(to);
		helper.setText("<b>Dear friend</b>,<br><i>Here's your invoice.</i>", true);

	    FileSystemResource file = new FileSystemResource(latestFile);
	    helper.addAttachment(latestFile.getName(), file);

		mailSender.send(message);
		return ResponseEntity.ok().body("Email has been sent successfully!");		
	}

    //Send Email via Crypto Currency
    public ResponseEntity<?> sendCryptoEmail(String transactionMessage, String renterAddress) throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "";
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                currentPrincipalName = ((UserDetails) principal).getUsername();
            } else if (principal instanceof CustomOAuth2User) {
                currentPrincipalName = ((CustomOAuth2User) principal).getName();
            } else {
                currentPrincipalName = principal.toString();
            }
        }
        User user = userRepository.findByUsername(currentPrincipalName);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String to = user.getEmail();
        String from = "viettravel2509@gmail.com";

        UserEmailCrypto userEmailCrypto = new UserEmailCrypto();
        userEmailCrypto.setUserName(currentPrincipalName);
        userEmailCrypto.setEmail(to);
        userEmailCrypto.setAddressAccount(renterAddress);
        userEmailCryptoServiceImpl.save(userEmailCrypto);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Your Information From VietTravel");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText(
                "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                        + "<h2 style='color: #2E86C1;'>Hello " + user.getUsername() + ",</h2>"
                        + "<p style='font-size: 16px;'>Thank you very much for using <strong>VietTravel</strong> products!</p>"
                        + "<hr style='border: 1px solid #ddd;'>"
                        + "<h3 style='color: #28B463;'>Crypto Currency Payment Information:</h3>"
                        + "<p style='font-size: 16px; background: #f8f9fa; padding: 10px; border-left: 4px solid #28B463;'>"
                        + transactionMessage + "</p>"
                        + "<p style='font-size: 16px;'>If you need immediate assistance, please call us at "
                        + "<a href='tel:+84966680989' style='color: #E74C3C; text-decoration: none; font-weight: bold;'>+84966.68.0989</a>.</p>"
                        + "<p style='font-size: 16px;'><strong>Best regards,</strong></p>"
                        + "<p style='font-size: 16px; color: #2E86C1;'><strong>VietTravel Support Team</strong></p>"
                        + "</div>",
                true);
        mailSender.send(message);
        return ResponseEntity.ok().body("Email has been sent successfully!");
    }

    //Send Email Event via Crypto Currency
    public ResponseEntity<?> sendCryptoEmailEvent(String transactionMessage, String payerAddress) throws MessagingException {
        UserEmailCrypto entity = userEmailCryptoRepository.findUserEmailCrypto(payerAddress);
        String to = entity.getEmail();
        String from = "viettravel2509@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Your Information From VietTravel");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText(
                "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>"
                        + "<h2 style='color: #2E86C1;'>Hello " + entity.getUserName() + ",</h2>"
                        + "<p style='font-size: 16px;'>Thank you very much for using <strong>VietTravel</strong> products!</p>"
                        + "<hr style='border: 1px solid #ddd;'>"
                        + "<h3 style='color: #28B463;'>Crypto Currency Payment Information:</h3>"
                        + "<p style='font-size: 16px; background: #f8f9fa; padding: 10px; border-left: 4px solid #28B463;'>"
                        + transactionMessage + "</p>"
                        + "<p style='font-size: 16px;'>If you need immediate assistance, please call us at "
                        + "<a href='tel:+84966680989' style='color: #E74C3C; text-decoration: none; font-weight: bold;'>+84966.68.0989</a>.</p>"
                        + "<p style='font-size: 16px;'><strong>Best regards,</strong></p>"
                        + "<p style='font-size: 16px; color: #2E86C1;'><strong>VietTravel Support Team</strong></p>"
                        + "</div>",
                true);
        mailSender.send(message);
        return ResponseEntity.ok().body("Email has been sent successfully!");
    }

    //Send Email Event PDF via Crypto Currency
    public ResponseEntity<?> sendHTMLEmailWithAttachmentCrypto(String payerAddress) throws MessagingException {
        UserEmailCrypto entity = userEmailCryptoRepository.findUserEmailCrypto(payerAddress);
        //Danh sách các File PDF đã lưu
        String directoryPath = "C:\\Users\\ADMIN\\eclipse-workspace\\VIETTRAVELBK\\src\\main\\resources\\static\\FilePDF\\FileExportPDF";
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.startsWith("InvoiceVietTravel_" + entity.getUserName()) && name.endsWith(".pdf"));
        if (files == null || files.length == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No invoice file found for user.");
        }

        //Sắp xếp lại các File để tìm kiếm File có thời gian muộn nhất
        File latestFile = Collections.max(Arrays.asList(files), Comparator.comparingLong(File::lastModified));

        String from = "viettravel2509@gmail.com";
        String to = entity.getEmail();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("Your Invoice From VietTravel");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<b>Dear friend</b>,<br><i>Here's your invoice.</i>", true);

        FileSystemResource file = new FileSystemResource(latestFile);
        helper.addAttachment(latestFile.getName(), file);

        mailSender.send(message);
        return ResponseEntity.ok().body("Email has been sent successfully!");
    }
}
