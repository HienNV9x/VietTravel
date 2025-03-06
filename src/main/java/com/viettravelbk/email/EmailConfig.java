package com.viettravelbk.email;

import java.io.File;
import java.util.Arrays;														
import java.util.Collections;													
import java.util.Comparator;													
import java.util.List;															

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
        helper.setText("<b>Hello " + user.getUsername() + "," + "</b><br>" +
        			   "<p>Thank you very much for using VietTravel products! We confirm that you have registered to use the following services:</p>" +
        			   "<p>Room booked: " + lastRevenue.getTitle_room() + "</p>" +
        			   "<p>Sites: " + lastRevenue.getCategory_room() + "</p> "     +
        			   "<p>Cuisine: " + lastRevenue.getCuisineLocal() + "</p>" +
        			   "<p>Intertainment: " + lastRevenue.getIntertainmentLocal() +
        			   "<p>Transport: " + lastRevenue.getMoveLocal() + 
        			   "<p>The mount paid: "+ lastRevenue.getIncome() + " $</p>" +
        			   "<p>After you Click Button \"PAY\", the payment invoice will be sent to your Email.</p>" + 
        			   "<p>If you need immediate assistance, please call us at +84966.68.0989.</p>" + 
        			   "<p>Best regards.</p>",html);

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
    public ResponseEntity<?> sendCryptoEmail(String transactionMessage) throws MessagingException {
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

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Your Information From VietTravel");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText("<b>Hello " + user.getUsername() + "," + "</b><br>" +
                "<p>Thank you very much for using VietTravel products! Crypto Currency Payment Information:</p>" +
                "<p>" + transactionMessage + "</p>" +
                "<p>If you need immediate assistance, please call us at +84966.68.0989.</p>" +
                "<p>Best regards.</p>",html);
        mailSender.send(message);
        return ResponseEntity.ok().body("Email has been sent successfully!");
    }

    public ResponseEntity<?> sendCryptoEmailEvent(String transactionMessage) throws MessagingException {
        System.out.println("Hello3");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication2: " + authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không thể xác định người dùng.");
        }
        String currentPrincipalName = "";
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            currentPrincipalName = ((UserDetails) principal).getUsername();
        } else if (principal instanceof CustomOAuth2User) {
            currentPrincipalName = ((CustomOAuth2User) principal).getName();
        } else {
            currentPrincipalName = principal.toString();
        }
        User user = userRepository.findByUsername(currentPrincipalName);
        System.out.println("user: " + user);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String to = user.getEmail();
        String from = "viettravel2509@gmail.com";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Your Information From VietTravel");
        helper.setFrom(from);
        helper.setTo(to);

        boolean html = true;
        helper.setText("<b>Hello " + user.getUsername() + "," + "</b><br>" +
                "<p>Thank you very much for using VietTravel products! Crypto Currency Payment Information:</p>" +
                "<p>" + transactionMessage + "</p>" +
                "<p>If you need immediate assistance, please call us at +84966.68.0989.</p>" +
                "<p>Best regards.</p>",html);
        mailSender.send(message);
        return ResponseEntity.ok().body("Email has been sent successfully!");
    }
}
