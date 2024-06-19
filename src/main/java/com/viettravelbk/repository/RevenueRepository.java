package com.viettravelbk.repository;

import java.util.Date;										
import java.util.List;												

import org.springframework.data.domain.Pageable;					
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;		
import org.springframework.data.repository.query.Param;		
import com.viettravelbk.model.Revenue;

public interface RevenueRepository extends JpaRepository<Revenue, Long>{
	//Lấy tổng số bản ghi trong table revenue
	long count(); 							
	
	//Lấy income theo ngày
	@Query("SELECT SUM(r.income) FROM Revenue r WHERE FUNCTION('DATE', r.createdDate) = :date")
	Double findTotalIncomeByDate(@Param("date") Date date);
	
	//Lấy income theo tháng
	@Query("SELECT SUM(r.income) FROM Revenue r WHERE MONTH(r.createdDate) = :month AND YEAR(r.createdDate) = :year")
	Double findTotalIncomeByMonth(@Param("month") int month, @Param("year") int year);
	
	//Lấy bản ghi có createdBy mới nhất 
	@Query("SELECT r FROM Revenue r WHERE r.createdBy = ?1 ORDER BY r.createdDate DESC")
	List<Revenue> findByCreatedByOrderByCreatedDateDesc(String createdBy, Pageable pageable);
}
