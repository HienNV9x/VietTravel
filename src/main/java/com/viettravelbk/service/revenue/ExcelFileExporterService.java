package com.viettravelbk.service.revenue;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettravelbk.model.Revenue;
import com.viettravelbk.repository.RevenueRepository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ExcelFileExporterService {							//Download File Excel from Table Revenue
    @Autowired
    private RevenueRepository revenueRepository;

    public void exportRevenueDataToExcelFile(HttpServletResponse response) throws IOException {
        List<Revenue> revenues = revenueRepository.findAll();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Revenues");
        CreationHelper createHelper = workbook.getCreationHelper();

        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        //Extending the header with new column names
        String[] columnNames = {"ID", "Room Name", "Province", "Income", "Quantity of Rooms", "User", "Created Date"};
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(headerStyle);
        }
        
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
        
        //Adding data for new columns
        int rowNum = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for (Revenue revenue : revenues) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(revenue.getId());
            row.createCell(1).setCellValue(revenue.getTitle_room());
            row.createCell(2).setCellValue(revenue.getCategory_room());
            row.createCell(3).setCellValue(revenue.getIncome());
            row.createCell(4).setCellValue(revenue.getQuantity());
            row.createCell(5).setCellValue(revenue.getCreatedBy());
            
            Cell dateCell = row.createCell(6);
            if (revenue.getCreatedDate() != null) {
                dateCell.setCellValue(sdf.format(revenue.getCreatedDate()));
                dateCell.setCellStyle(dateCellStyle);
            }
        }
        
        //Auto-size columns
        for (int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        //Setting the content type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=revenues.xlsx");
        
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        outputStream.close();
    }
}
