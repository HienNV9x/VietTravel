package com.viettravelbk.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viettravelbk.dto.RevenueDTO;
import com.viettravelbk.repository.RevenueRepository;
import com.viettravelbk.service.revenue.ExcelFileExporterService;
import com.viettravelbk.service.revenue.RevenueService;

@RestController
public class RevenueAPI {
	@Autowired
	private RevenueService revenueService;
	@Autowired
	private RevenueRepository revenueRepository;
    @Autowired
    private ExcelFileExporterService excelFileExporterService;
	
    @GetMapping(value = "/revenue")													
    public ResponseEntity<List<RevenueDTO>> showRevenue() {
        List<RevenueDTO> revenues = revenueService.findAll();
        return new ResponseEntity<>(revenues, HttpStatus.OK);
    }
    
    @PostMapping("/revenue")									
    public RevenueDTO createRevenue(@RequestBody RevenueDTO model) {
            return revenueService.save(model);
    }
    
    //Lấy tổng income từ database
    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalIncome() {
        double totalIncome = (double)revenueService.getTotalIncome();
        return new ResponseEntity<>(totalIncome, HttpStatus.OK);
    }
    
    //Lấy tổng số bản ghi trong table revenue
    @GetMapping("/revenue/count")
    public ResponseEntity<Long> getTotalRecords() {
        long totalRecords = revenueService.getTotalRecords();
        return new ResponseEntity<>(totalRecords, HttpStatus.OK);
    }
    
    //Lấy tổng quantity từ database
    @GetMapping("/revenue/quantity")
    public ResponseEntity<Integer> getTotalQuantity() {
        int totalQuantity = revenueService.getTotalQuantity();
        return new ResponseEntity<>(totalQuantity, HttpStatus.OK);
    }
    
    //Lấy income theo ngày
    @GetMapping("/revenue/dailyIncome")
    public ResponseEntity<Double> getIncomeByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    	double dailyIncome = (double)revenueService.getIncomeByDate(date);
        return new ResponseEntity<>(dailyIncome, HttpStatus.OK);
    }
    
    //Lấy income theo tháng
    @GetMapping("/revenue/monthlyIncome")
    public ResponseEntity<Double> getIncomeByMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        double monthlyIncome = (double)revenueService.getIncomeByMonth(month, year);
        return new ResponseEntity<>(monthlyIncome, HttpStatus.OK);
    }
    
    //Download file excel revenue
    @GetMapping("/export/revenues/excel")
    public void exportRevenueDataToExcel(HttpServletResponse response) throws IOException {
        excelFileExporterService.exportRevenueDataToExcelFile(response);
    }
}
