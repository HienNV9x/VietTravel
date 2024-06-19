package com.viettravelbk.email;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;											
import java.util.List;
import java.text.SimpleDateFormat;								
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;							
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;							
import com.itextpdf.text.pdf.ColumnText;					
import com.itextpdf.text.pdf.PdfContentByte;				
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;						
import com.itextpdf.text.pdf.PdfStamper;					
import com.itextpdf.text.pdf.PdfWriter;
import com.viettravelbk.model.Revenue;
import com.viettravelbk.model.User;
import com.viettravelbk.repository.RevenueRepository;
import com.viettravelbk.repository.UserRepository;
import com.viettravelbk.service.oauth2.CustomOAuth2User;

@RestController
public class ExportPDF {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RevenueRepository revenueRepository;
	
	//Chuyển số tiền từ số sang chữ
	public static String convertNumberToWords(double amount) {
	    if (amount == 0) return "Zero dollars";
	    long dollars = (long) amount;
	    int cents = (int)Math.round((amount - dollars) * 100);
	    String dollarPart = NumberToWords.convert((int) dollars) + (dollars == 1 ? " dollar" : " dollars");
	    String centPart = cents > 0 ? " and " + NumberToWords.convert(cents) + (cents == 1 ? " cent" : " cents") : "";
	    return dollarPart + centPart;
	}
	
	//Phương thức để thêm hàng trống
	private static void addEmptyRow(PdfPTable table) {

	    //Thiết lập chiều cao hàng mặc định hoặc dựa theo yêu cầu
	    float emptyRowHeight = 23.65f;  					//Có thể thay đổi chiều cao này tùy ý

	    //Tạo 5 ô trống với chiều cao và colspan phù hợp
	    PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
	    emptyCell.setFixedHeight(emptyRowHeight);
	    emptyCell.setColspan(1);  							//Mỗi ô chỉ chiếm 1 cột

	    //Thêm ô trống vào 5 lần để đạt 5 cột
	    for (int i = 0; i < 5; i++) {
	        table.addCell(emptyCell);
	    }
	}
	
	//Phương thức để thêm hàng với hai ô trống có chiều rộng khác nhau
	private static void addCustomEmptyRow(PdfPTable table) {
	    //Tạo ô trống đầu tiên với colspan là 2
	    PdfPCell cellOne = new PdfPCell(new Phrase(" "));
	    cellOne.setFixedHeight(23.65f); 					//Chiều cao cố định cho ô
	    //cellOne.setColspan(2); 							//Ô này chiếm 2 cột

	    //Tạo ô trống thứ hai với colspan là 3
	    PdfPCell cellTwo = new PdfPCell(new Phrase(" "));
	    cellTwo.setFixedHeight(23.65f); 					//Chiều cao cố định cho ô
	    //cellTwo.setColspan(3); 							//Ô này chiếm 3 cột

	    // Thêm các ô vào bảng
	    table.addCell(cellOne);
	    table.addCell(cellTwo);
	}
	
	@GetMapping("/export_file_pdf")
    public void exportFilePDF() throws DocumentException, IOException {        
    	//Lấy đối tượng đang đăng nhập	
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = "";
        if (authentication != null) {						//Xác thực đối tượng có đang đăng nhập hay ko?
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
        
        //Lấy thông tin revenue mới nhất được tạo bởi người dùng hiện tại
        List<Revenue> revenues = revenueRepository.findByCreatedByOrderByCreatedDateDesc(currentPrincipalName, PageRequest.of(0, 1));
        Revenue lastRevenue = revenues.isEmpty() ? null : revenues.get(0);
		
        //Địa chỉ lấy File PDF mẫu và địa chỉ + tên File PDF kết quả
		PdfReader reader = new PdfReader("C:\\Users\\ADMIN\\eclipse-workspace\\VIETTRAVELBK\\src\\main\\resources\\static\\FilePDF\\Invoice.pdf");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String dateStr = sdf.format(new Date());
        String outputFilePath = "C:\\Users\\ADMIN\\eclipse-workspace\\VIETTRAVELBK\\src\\main\\resources\\static\\FilePDF\\FileExportPDF\\InvoiceVietTravel_" + user.getUsername() + "-" + dateStr + ".pdf";
        
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFilePath));
        PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());			//Lấy nội dung của trang cuối cùng
        
        //Chuyển số tiền từ số sang chữ
        String revenueInWords = convertNumberToWords(lastRevenue.getIncome());
        
        //Chèn chuỗi ký tự
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        Phrase client_name = new Phrase(user.getUsername(), font);
        Phrase client_location = new Phrase("HaNoi, VietNam", font);
        Phrase client_signature = new Phrase(user.getUsername(), font);
        Phrase revenuePhrase = new Phrase(revenueInWords, font);
        Phrase totalRevenue_1 = new Phrase("TOTAL", font);
        Phrase totalRevenue_2 = new Phrase(String.valueOf(lastRevenue.getIncome()) + "$", font);
        
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, client_name, 168, 692, 0); //113 là 4cm tính bằng point, 730 là 5cm tính bằng point từ trên xuống
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, client_location, 136, 670, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, client_signature, 112, 92, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, revenuePhrase, 244, 178, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, totalRevenue_1, 122, 237, 0);
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, totalRevenue_2, 230, 237, 0);
        
        //Chèn chuỗi Ngày - Tháng - Năm
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        Date currentDate = new Date();
        String day = dayFormat.format(currentDate);
        String month = monthFormat.format(currentDate);
        String year = yearFormat.format(currentDate);

        String dateText = "" + day + "            " + month + "           " + year;
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        Phrase datePhrase = new Phrase(dateText, dateFont);

        //Xác định vị trí cụ thể để chèn chuỗi ngày tháng; ví dụ: (x, y) = (50, 750)
        ColumnText.showTextAligned(content, Element.ALIGN_LEFT, datePhrase, 379, 140, 0);
        
        //Tạo một bảng với 5 cột
        PdfPTable table = new PdfPTable(5);
        float firstColumnWidth = 45.29f; 						//chiều rộng cột đầu tiên
        float secondColumnWidth = 91.40f;						//chiều rộng cột thứ 2
        float thirdColumnWidth = 198.50f;
        float fourthColumnWidth = 62.30f;
        float otherColumnWidth = (595 - 72.00f - 56.69f - firstColumnWidth - secondColumnWidth - thirdColumnWidth - fourthColumnWidth); // Phân bổ chiều rộng còn lại cho 4 cột khác
        table.setWidths(new float[]{firstColumnWidth, secondColumnWidth, thirdColumnWidth, fourthColumnWidth, otherColumnWidth}); // Thiết lập chiều rộng cho các cột

        //Thêm hàng mới vào bảng
        addRow(table, "1", "Book Room", lastRevenue.getTitle_room(), String.valueOf(lastRevenue.getQuantity()) , String.valueOf(lastRevenue.getIncome()) + "$" );
        addRow(table, "2", "Book cuisine areas", lastRevenue.getCuisineLocal(), "", "10% Discount");
        addRow(table, "3", "Set entertainment areas", lastRevenue.getIntertainmentLocal(), "", "10% Discount");
        addRow(table, "4", "Rent transportation", lastRevenue.getMoveLocal(), "", "5% Discount");
        
        //Chiều rộng trang A4 là 595 points
        float pageWidth = 595;
        float marginLeft = 72.00f; 								
        float marginRight = 56.69f; 
        float tableWidth = pageWidth - marginLeft - marginRight;

        //Đặt chiều rộng bảng
        table.setTotalWidth(tableWidth);

        //Vị trí bắt đầu của bảng (cách lề trái 5cm)
        float x = marginLeft;
        float y = 615.00f; 										//Vị trí dọc từ dưới lên mà bạn muốn đặt bảng mới

        // Đặt bảng vào vị trí mong muốn
        table.setTotalWidth(483.30f); 							//Chiều rộng của bảng
        
        table.calculateHeights();  // Tính toán chiều cao của bảng
        float totalHeight = table.getTotalHeight();  			//Lấy chiều cao tổng

        // Kiểm tra nếu chiều cao chưa đạt 15cm
        float requiredHeight = 340; 							//34cm = 340 points
        if (totalHeight < requiredHeight) {
            //Thêm bảng trống để chiều cao tổng đạt 15cm
            while (totalHeight < requiredHeight) {
                addEmptyRow(table);
                table.calculateHeights();
                totalHeight = table.getTotalHeight();
            }
        }
        
        //Giả sử bạn đã có độ rộng của từng cột dưới dạng mảng
        float[] columnWidths = new float[]{firstColumnWidth, secondColumnWidth, thirdColumnWidth, fourthColumnWidth, otherColumnWidth};
        //Tính toán tổng độ rộng cho ô đầu tiên và ô thứ hai
        float firstTwoColumnsWidth = columnWidths[0] + columnWidths[1];
        float lastThreeColumnsWidth = columnWidths[2] + columnWidths[3] + columnWidths[4];
        //Sau khi các bảng khác đã được thêm vào, gọi hàm này để thêm bảng cuối cùng với hai ô trống
        PdfPTable customTable = new PdfPTable(2); 				//Tạo bảng mới với 5 cột
        customTable.setTotalWidth(new float[]{firstTwoColumnsWidth, lastThreeColumnsWidth});
        customTable.setTotalWidth(483.30f);
        addCustomEmptyRow(customTable); 						//Thêm hàng với hai ô trống vào bảng
        
        
        table.writeSelectedRows(0, -1, x, y, content); 			//Đặt bảng vào vị trí mong muốn
        customTable.writeSelectedRows(0, -1, marginLeft, 254, content); //Đặt bảng cuối cùng TOTAL
        stamper.close();
        reader.close();
    }

    private static void addRow(PdfPTable table, String id, String title, String firstName, String lastName, String additionalInfo) {
        PdfPCell cell1 = new PdfPCell(new Phrase(id));
        PdfPCell cell2 = new PdfPCell(new Phrase(title));
        PdfPCell cell3 = new PdfPCell(new Phrase(firstName));
        PdfPCell cell4 = new PdfPCell(new Phrase(lastName));
        PdfPCell cell5 = new PdfPCell(new Phrase(additionalInfo));
        
        //Enable wrapping
        cell3.setNoWrap(false); 								//Cài đặt này cho phép nội dung tự động xuống dòng

        //Set horizontal alignment
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        //Set vertical alignment
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
    }
}
