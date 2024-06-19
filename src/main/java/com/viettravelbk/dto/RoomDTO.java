package com.viettravelbk.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoomDTO extends AbstractDTO<RoomDTO>{		//Nhận data từ client dưới dạng json và truyền cho model
    private String title;								//post put delete get
    private String content;  
    private String shortDescription;
    private String thumbnail;
    private String roomService;
    private Double roomPoint;
    private String userIdLike;
    private double price;
    private String categoryCode;
    private String categoryName;						//Thêm field Name trong Table Category
    private Long imageId;								//id Table image
    private String imageUrl;							//Thêm thuộc tính imageUrl để lưu đường dẫn hình ảnh
    private List<String> imageUrls;						//Lưu đường dẫn 5 hình ảnh
}
