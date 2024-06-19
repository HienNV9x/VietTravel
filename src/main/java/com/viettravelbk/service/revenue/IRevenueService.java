package com.viettravelbk.service.revenue;

import java.time.LocalDate;
import java.util.List;
import com.viettravelbk.dto.RevenueDTO;

public interface IRevenueService {
	RevenueDTO save(RevenueDTO revenueDTO);					
	List<RevenueDTO> findAll();
	double getTotalIncome();						//Lấy tổng income từ database
	long getTotalRecords();							//Lấy tổng số bản ghi trong table revenue
	int getTotalQuantity();							//Lấy tổng số phòng đã sử dụng
	double getIncomeByDate(LocalDate date);			//Lấy income theo ngày
	double getIncomeByMonth(int month, int year);	//Lấy income theo tháng
}
