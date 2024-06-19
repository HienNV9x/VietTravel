package com.viettravelbk.service.revenue;

import java.time.LocalDate;						
import java.time.ZoneId;						
import java.time.format.DateTimeFormatter;		
import java.time.format.DateTimeParseException;	
import java.util.ArrayList;
import java.util.Date;							
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.viettravelbk.model.User;
import com.viettravelbk.converter.RevenueConverter;
import com.viettravelbk.dto.RevenueDTO;
import com.viettravelbk.model.Revenue;
import com.viettravelbk.repository.RevenueRepository;
import com.viettravelbk.repository.UserRepository;


@Service
public class RevenueService implements IRevenueService{
	@Autowired
	private RevenueRepository revenueRepository;
	@Autowired
	private RevenueConverter revenueConverter;
    @Autowired
    private UserRepository userRepository;
    
	@Override
	public RevenueDTO save(RevenueDTO revenueDTO) {				//post
		Revenue revenueEntity = revenueConverter.toEntityRevenue(revenueDTO);
	
        //Lấy thông tin người dùng đăng nhập từ SecurityContext
	    if (revenueDTO.getId() != null) {
	        Optional<Revenue> optionalOldRevenue = revenueRepository.findById(revenueDTO.getId());
	        if (optionalOldRevenue.isPresent()) {
	        	revenueEntity = revenueConverter.toEntityRevenue(revenueDTO, optionalOldRevenue.get());
	        }
	    }
	    revenueEntity = revenueRepository.save(revenueEntity); 	//Lưu commentEntity vào database
	    return revenueConverter.toDTORevenue(revenueEntity); 	//Trả về DTO từ entity đã lưu
	}
	
	@Override
	public List<RevenueDTO> findAll() {							//lấy tất cả các trang
		List<RevenueDTO> resultss = new ArrayList<>();
		List<Revenue> entitiess = revenueRepository.findAll();	//Truyền pageable xuống entity
		for (Revenue item: entitiess) {
			RevenueDTO revenueDTO = revenueConverter.toDTORevenue(item);
			resultss.add(revenueDTO);
		}
		return resultss;
	}
	
	//Lấy tổng income từ database
	@Override
	public double getTotalIncome() {
	    return revenueRepository.findAll().stream().mapToDouble(Revenue::getIncome).sum();
	}
	
	//Lấy tổng số bản ghi trong table revenue
    @Override
    public long getTotalRecords() {
        return revenueRepository.count();
    }
    
    //Lấy tổng số phòng đã sử dụng
    @Override
    public int getTotalQuantity() {
    	return revenueRepository.findAll().stream().mapToInt(Revenue::getQuantity).sum();
    }
    
    //Lấy income theo ngày sau khi đã được chuyển đổi
    @Override
    public double getIncomeByDate(LocalDate date) {
        // Chuyển đổi LocalDate sang java.util.Date
        Date dateUtil = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return revenueRepository.findTotalIncomeByDate(dateUtil);
    }
    
    //Lấy income theo tháng
    @Override
    public double getIncomeByMonth(int month, int year) {
        Double totalIncome = revenueRepository.findTotalIncomeByMonth(month, year);
        return totalIncome != null ? totalIncome : 0;
    }
    
}
