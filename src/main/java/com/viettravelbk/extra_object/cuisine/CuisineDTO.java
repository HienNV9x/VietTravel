package com.viettravelbk.extra_object.cuisine;

import lombok.Data;

@Data
public class CuisineDTO {
	private Long id;
	private String titleCuisine;
	private String contentCuisine;
	private String locationCuisine;
	private String thumbnailCuisine;
    private String categoryCode;
    private String categoryName;						//Thêm field Name trong Table Category
    private Long imageId;								//id Table image
    private String imageUrl;							//Thêm thuộc tính imageUrl để lưu đường dẫn hình ảnh
    private String imageUrls;							//Lưu đường dẫn 5 File ảnh của 1 Object ImageInfo
}
